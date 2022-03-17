package uk.ac.soton.comp2211.model;

import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.exceptions.RunwayException;

public class Runway {
    private String runwayDesignator;
    private Tarmac tarmac; // Corresponding physical runway.
    private RunwayValues originalValues;
    private RunwayValues currentValues;
    private int displacedThreshold;
    private int stopway;
    private int clearway;

    private int RESA = 240; // runway end safety area (meters)
    private int SEV = 60; // strip end value (meters)
    private int ALS = 50; // approach landing surface slope
    private int TOCS = 50; // take-off climb surface slope
    
    private int length;
    private int width;

    public Runway(String _runwayDesignator, Tarmac _tarmac, RunwayValues _originalValues, 
                  int _displacedThreshold, int _stopway, int _clearway) {
        runwayDesignator = _runwayDesignator;
        tarmac = _tarmac;
        originalValues = _originalValues;
        currentValues = _originalValues.clone();
        displacedThreshold = _displacedThreshold;
<<<<<<< Updated upstream
        
        length = originalValues.getTORA();
        width = 60;
=======
        stopway = _stopway;
        clearway = _clearway;
>>>>>>> Stashed changes
    }

    public String getRunwayDesignator() { return runwayDesignator; }
    public Tarmac getTarmac() { return tarmac; }
    public RunwayValues getOriginalValues() { return originalValues; }
    public RunwayValues getCurrentValues() { return currentValues; }
    public int getDisplacedThreshold() { return displacedThreshold; }
    public int getStopway() { return stopway; }
    public int getClearway() { return clearway; }

    public void recalculate(int _blastAllowance) throws RunwayException, PositionException {
        Obstacle obstacle = tarmac.getObstacle();

        // If there is no obstacle on the tarmac, then there is no need to recalculate runway values.
        if (obstacle == null) return;

        int obstacleLength = obstacle.getLength(); // not used at the moment
        int obstacleHeight = obstacle.getHeight();

        int obstaclePosition;
        if (runwayDesignator.contains("09")) 
            obstaclePosition = obstacle.getPosition().getDistanceFromWest();
        else if (runwayDesignator.contains("27"))
            obstaclePosition = obstacle.getPosition().getDistanceFromEast();
        else throw new RunwayException("Couldn't recalculate runway values," +
                                       "due to an incorrect runway designator!");

        if (obstaclePosition <= originalValues.getTORA() / 2) 
            recalculateObstacleCloser(_blastAllowance, obstacleHeight, obstaclePosition);
        else recalculateObstacleFurther(_blastAllowance, obstacleHeight, obstaclePosition);
    }

    private void recalculateObstacleCloser(int _blastAllowance, 
                                           int _obstacleheight, 
                                           int _obstaclePosition) {
        //closer to start of runway, can replace tarmac.getLength() with originalValues.getTORA()

            // landing over obstacle
            int newLDA;
            int d1 = _obstaclePosition + (_obstacleheight * ALS) + SEV;
            int d2 = _obstaclePosition + RESA + SEV; // including obsLength?
            int d3 = _obstaclePosition + _blastAllowance;
            newLDA = Math.max(d1, Math.max(d2, d3));
            currentValues.setLDA(originalValues.getLDA() - newLDA);

            // take off away from obstacle
            int d4 = _obstaclePosition + _blastAllowance + getDisplacedThreshold(); // include obsLength?
            currentValues.setTORA(originalValues.getTORA() - d4);
            currentValues.setTODA(currentValues.getTORA() + originalValues.getClearway());
            currentValues.setASDA(currentValues.getTORA() + originalValues.getStopway());
    }

    private void recalculateObstacleFurther(int _blastAllowance,
                                            int _obstacleHeight, 
                                            int _obstaclePosition) {
        //closer to end of runway

            //land towards obstacles
            currentValues.setLDA(_obstaclePosition - RESA - SEV);

            // take off towards it
            int d1 = _obstaclePosition - RESA - SEV;
            int d2 = _obstaclePosition - (_obstacleHeight * TOCS) - SEV + getDisplacedThreshold();
            currentValues.setTORA(Math.min(d1, d2));
            // TODA = ASDA = TORA
            currentValues.setTODA(currentValues.getTORA());
            currentValues.setASDA(currentValues.getTORA());
    }
    
    public int getLength() {
        return this.length;
    }
    
    public int getWidth() {
        return this.width;
    }
}