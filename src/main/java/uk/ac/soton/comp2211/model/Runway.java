package uk.ac.soton.comp2211.model;

import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.exceptions.RunwayException;

import java.util.ArrayList;

public class Runway {
    public static final int MIN_TORA = 1;
    public static final int MAX_TORA = 5000;
    public static final int MIN_TODA = 1;
    public static final int MAX_TODA = 10000;
    public static final int MIN_ASDA = 1;
    public static final int MAX_ASDA = 10000;
    public static final int MIN_LDA = 1;
    public static final int MAX_LDA = 5000;

    private String runwayDesignator;
    private Tarmac tarmac; // Corresponding tarmac
    private boolean alt; // is an alternate runway

    private RunwayValues originalValues;
    private RunwayValues currentValues;

    // potentially modifiable global variables
    private int RESA = 240; // runway end safety area (meters)
    private int SEV = 60; // strip end value (meters)
    private int ALS = 50; // approach landing surface slope
    private int TOCS = 50; // take-off climb surface slope
    private int CLDT = 75; // threshold for distance from centerline
    private int TDT = 60; // threshold for distance from threshold

    // visualization variables
    private int length;
    private int width;
    private int obstaclePositionReal;

    public Runway(String _runwayDesignator, Tarmac _tarmac, RunwayValues _originalValues) {
        runwayDesignator = _runwayDesignator;
        tarmac = _tarmac;

        if (Integer.parseInt(_runwayDesignator.substring(0,2))<=18) {
            alt = false;
        } else {
            alt = true;
        }

        originalValues = _originalValues;
        currentValues = _originalValues.clone();

        length = originalValues.getTORA(); // setting length to the original TORA value
        width = 60; //TODO default width?

    }

    // handling if the runway is primary or secondary
    public void setAlt(boolean x) {alt = x;}
    public boolean isAlt() { return alt;}

    public String getRunwayDesignator() { return runwayDesignator; }
    public Tarmac getTarmac() { return tarmac; }
    public RunwayValues getOriginalValues() { return originalValues; }
    public RunwayValues getCurrentValues() { return currentValues; }

    public int getLength() { return this.length; }
    public int getWidth() { return this.width; }

    public void reset() { this.currentValues = this.originalValues.clone(); }

    public ArrayList<String> recalculate (int _blastAllowance) throws RunwayException, PositionException {
        ArrayList<String> returnList = new ArrayList<>();
        Obstacle obstacle = tarmac.getObstacle();

        // fixed messages
        String noChanges = "no changes were made to runway parameters";
        String obsCloser = "obstacle is closer to the threshold of the runway";
        String obsFurther = "obstacle is closer to the end of the runway";


        // If there is no obstacle on the tarmac
        if (obstacle == null) {
            returnList.add(noChanges);
            returnList.add("reason: no obstacle present");
            return returnList;
        }

        int obstacleHeight = obstacle.getHeight(); // get obstacle height

        // get obstacle position
        int obstaclePosition;
        obstaclePositionReal = obstacle.getPosition().getDistance();
        if (!isAlt()) {
            obstaclePosition = obstacle.getPosition().getDistance(); // if main runway
        } else {
            obstaclePosition = this.getLength() - obstaclePositionReal; // if alt runway
        }

        // check if redeclaration is not needed if obstacle is far enough before or after the runway
        if (obstaclePosition < (-1 * TDT)) {
            returnList.add(noChanges);
            returnList.add(String.format("reason: obstacle is more than %d from the start of the runway", TDT));
            return returnList;
        }
        if (obstaclePosition > getLength() + TDT) {
            returnList.add(noChanges);
            returnList.add(String.format("reason: obstacle is more than %d from the end of the runway", TDT));
            return returnList;
        }

        // check if redeclaration is not needed if obstacle is far enough from center line
        int obstacleDisplacement = obstacle.getPosition().getCentreLineDisplacement();
        if (obstacleDisplacement > CLDT) {
            returnList.add(noChanges);
            returnList.add(String.format("reason: obstacle is more than %d from the center line", CLDT));
            return returnList;
        }

        // redeclare runway parameters
        ArrayList<String> steps;
        if (obstaclePositionReal <= originalValues.getTORA() / 2) {
            returnList.add(obsCloser);
            steps = recalculateCloser(_blastAllowance, obstacleHeight, obstaclePositionReal);
        } else {
            returnList.add(obsFurther);
            steps = recalculateFurther(_blastAllowance, obstacleHeight, obstaclePositionReal);
        }

        returnList.addAll(steps);

        return returnList;
    }

    private ArrayList<String> recalculateCloser(int _blastAllowance, int _obstacleHeight, int _obstaclePosition) {
        ArrayList<String> returnList = new ArrayList<>();

        returnList.add("take off away from the obstacle");
        int d4 = _obstaclePosition + _blastAllowance + originalValues.getDT();
        returnList.add("TORA reduction = obstacle position + blast allowance + displaced threshold");
        returnList.add(String.format("%d = %d + %d + %d", d4, _obstaclePosition, _blastAllowance, originalValues.getDT()));

        currentValues.setTORA(originalValues.getTORA() - d4);
        currentValues.setTODA(currentValues.getTORA() + originalValues.getClearway());
        currentValues.setASDA(currentValues.getTORA() + originalValues.getStopway());

        returnList.add(String.format("TORA = TORA - TORA reduction = %d - %d", originalValues.getTORA(), currentValues.getTORA()));
        returnList.add(String.format("TODA = TORA + clearway = %d + %d", currentValues.getTODA(), originalValues.getClearway()));
        returnList.add(String.format("ASDA = TORA + stopway = %d + %d", currentValues.getASDA(), originalValues.getStopway()));

        returnList.add("land over the obstacle");
        int dMax;
        int d1 = _obstaclePosition + (_obstacleHeight * ALS) + SEV;
        returnList.add("possible LDA reduction 1 = obstacle position + (obstacle height * ALS) + SEV");
        returnList.add(String.format("%d = %d + (%d * %d) + %d", d1, _obstaclePosition, _obstacleHeight, ALS, SEV));

        int d2 = _obstaclePosition + RESA + SEV;
        returnList.add("possible LDA reduction 2 = obstacle position + RESA + SEV");
        returnList.add(String.format("%d = %d + %d + %d", d2, _obstaclePosition, RESA, SEV));

        int d3 = _obstaclePosition + _blastAllowance;
        returnList.add("possible LDA reduction 3 = obstacle position + blast allowance");
        returnList.add(String.format("%d = %d + %d", d3, _obstaclePosition, _blastAllowance));

        dMax = Math.max(d1, Math.max(d2, d3));
        currentValues.setLDA(originalValues.getLDA() - dMax);
        returnList.add("LDA = LDA - greatest possible reduction");
        returnList.add(String.format("%d = %d - %d", currentValues.getLDA(), originalValues.getLDA(), dMax));

        return returnList;
    }

    private ArrayList<String> recalculateFurther(int _blastAllowance, int _obstacleHeight, int _obstaclePosition) {
        ArrayList<String> returnList = new ArrayList<>();

        returnList.add("take off towards the obstacle");
        int d1 = _obstaclePosition - RESA - SEV + originalValues.getDT();
        returnList.add("possible TORA 1 = obstacle distance from threshold - RESA - SEV + displaced threshold");
        returnList.add(String.format("%d = %d - %d - %d + %d", d1, _obstaclePosition, RESA, SEV, originalValues.getDT()));

        int d2 = _obstaclePosition - (_obstacleHeight * TOCS) - SEV + originalValues.getDT();
        returnList.add("possible TORA 2 = obstacle distance from threshold - (obstacle height * TOCS) - SEV + displaced threshold");
        returnList.add(String.format("%d = %d - (%d * %d) - %d + %d", d2, _obstaclePosition, _obstacleHeight, TOCS, SEV, originalValues.getDT()));

        currentValues.setTORA(Math.min(d1, d2));
        currentValues.setTODA(currentValues.getTORA());
        currentValues.setASDA(currentValues.getTORA());

        returnList.add(String.format("TORA = lower value of possible TORA 1 and possible TORA 2 = %d", currentValues.getTORA()));
        returnList.add(String.format("TODA = TORA because there is no clearway = %d", currentValues.getTODA()));
        returnList.add(String.format("ASDA = TORA because there is no stopway = %d", currentValues.getASDA()));

        returnList.add("land towards the obstacle");
        currentValues.setLDA(_obstaclePosition - RESA - SEV);
        returnList.add("LDA = obstacle position - RESA - SEV");
        returnList.add(String.format("%d = %d - %d - %d", currentValues.getLDA(), _obstaclePosition, RESA, SEV));

        return returnList;
    }
}