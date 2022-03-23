package uk.ac.soton.comp2211.model;

public class Tarmac {
    private final int id;
    private boolean bi; // is the tarmac bidirectional

    private Obstacle obstacle;
    private Runway runway;
    private Runway runwayAlt;

    public Tarmac(int _id) { id = _id; }

    public int getID() { return id; }

    public Obstacle getObstacle() { return obstacle; }

    public void setObstacle(Obstacle _obstacle) {
        obstacle = _obstacle;
        resetRunways();
    }

    public void removeObstacle() { setObstacle(null); }

    public void resetRunways() {
        runway.reset();
        if (runwayAlt != null) {runwayAlt.reset();}
    }

    public void setRunways(Runway[] _runways) {
        try {
            runway = _runways[0];
            runwayAlt = _runways[1];
            runway.setAlt(false);
            runwayAlt.setAlt(true);
            bi = true;
        } catch (ArrayIndexOutOfBoundsException e) {
            runway = _runways[0];
            runwayAlt = null;
            runway.setAlt(false);
            bi = false;
        }

    }

    public Runway[] getRunways() {
        if (bi) {
            return new Runway[] {runway, runwayAlt};
        } else {
            return new Runway[] {runway};
        }
    }
}
