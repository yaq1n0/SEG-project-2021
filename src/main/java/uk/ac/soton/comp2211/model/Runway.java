package uk.ac.soton.comp2211.model;

public class Runway {
    private String runwayDesignator;
    
    private RunwayValues originalValues;
    private RunwayValues currentValues;

    private Obstacle obstacle;
    
    public Runway(String _runwayDesignator, RunwayValues _runwayValues) {
        runwayDesignator = _runwayDesignator;

        originalValues = _runwayValues;
        currentValues = _runwayValues;
    }

    public String getRunwayDesignator() { return runwayDesignator; }

    public RunwayValues getOriginalValues() { return originalValues; }
    public RunwayValues getCurrentValues() { return currentValues; }

    public void setObstacle(Obstacle _obstacle) { obstacle = _obstacle; }
    public void removeObstacle(int _index) { obstacle = null; }
    public Obstacle getObstacles() { return obstacle; }

    /**
     * If takeOffOrLand is 0, then plane is taking off.
     *                  is 1, then plane is landing.
     * 
     * _aToB is true if the plance is moving from point A to point B on the runway.
     *       is false if the plance is moving from point B to point A on the runway.
     * 
     * @param takeOffOrLand
     * @param direction
     */
    public void calculateValues(int _takeOffOrLand, boolean _aToB) {
        if (obstacle == null) currentValues = originalValues.clone();
        else {
            boolean closerToA = (originalValues.getTODA() / 2) < obstacle.getPosition().getY();

            // Plane is taking 
            if (_takeOffOrLand == 0) {
                if (_aToB ^ closerToA) calculateTakeOffAwayFromTheObstacle();
                else calculateTakeOffTowardsTheObstacle();

            // Plane is landing.
            } else if (_takeOffOrLand == 1) {
                if (_aToB ^ closerToA) calculateLandingOverTheObstacle();
                else calculateLandingTowardsTheObstacle();
            }
        }
    }
    
    private void calculateLandingOverTheObstacle() {

    }

    private void calculateLandingTowardsTheObstacle() {

    }

    private void calculateTakeOffTowardsTheObstacle() {

    }

    private void calculateTakeOffAwayFromTheObstacle() {

    }
}