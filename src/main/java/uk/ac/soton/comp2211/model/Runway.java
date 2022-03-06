package uk.ac.soton.comp2211.model;

public class Runway {
    private String runwayDesignator;

    private Tarmac tarmac; // Corrosponding physical runway.

    private RunwayValues originalValues;
    private RunwayValues currentValues;

    private int displacedThreshold;
    
    public Runway(String _runwayDesignator, Tarmac _tarmac, RunwayValues _originalValues, int _displacedThreshold) {
        runwayDesignator = _runwayDesignator;

        tarmac = _tarmac;

        originalValues = _originalValues;
        currentValues = _originalValues.clone();

        displacedThreshold = _displacedThreshold;
    }

    public String getRunwayDesignator() { return runwayDesignator; }

    public Tarmac getTarmac() { return tarmac; }

    public RunwayValues getOriginalValues() { return originalValues; }
    public RunwayValues getCurrentValues() { return currentValues; }

    public int getDisplacedThreshold() { return displacedThreshold; }

    public void calculateValues(boolean takingOff, int blastAllowance) {
    }


    private int obstacleDistanceFromThreshold() {
        return 0;
    }

    private int calculateSlope() { return tarmac.getObstacle().getHeight() * 50; }
}