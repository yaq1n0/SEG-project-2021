package uk.ac.soton.comp2211.model;

public class Runway {
    private String runwayDesignator;

    private Tarmac tarmac; // Corrosponding physical runway.

    private RunwayValues originalValues;
    private RunwayValues currentValues;
    
    public Runway(String _runwayDesignator, Tarmac _tarmac, RunwayValues _originalValues) {
        runwayDesignator = _runwayDesignator;

        tarmac = _tarmac;

        originalValues = _originalValues;
        currentValues = _originalValues.clone();
    }

    public String getRunwayDesignator() { return runwayDesignator; }

    public Tarmac getTarmac() { return tarmac; }

    public RunwayValues getOriginalValues() { return originalValues; }
    public RunwayValues getCurrentValues() { return currentValues; }

    public void calculateValues(boolean takingOff, int blastAllowance) {
        if (tarmac.getObstacle() == null) currentValues.copy(originalValues);
        else {
            // currentValues.setLSA(originalValues.);
        }
    }


    private int distanceToObstacle() {
        return 0;
    }
}