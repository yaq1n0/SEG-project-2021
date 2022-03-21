package uk.ac.soton.comp2211.model;

public class Tarmac {
    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 1000;

    private int id;
    private int length;
    private Obstacle obstacle;

    private Runway[] runways;

    public Tarmac(int _id) { id = _id; }

    public int getID() { return id; }
    public int getLength() { return length; }

    public void setObstacle(Obstacle _obstacle) { 
        obstacle = _obstacle;
        for (Runway r : runways) {
            r.reset();
        }
    }
    public void removeObstacle() { 
        obstacle = null;
        for (Runway r : runways) {
            r.reset();
        }
    }
    public Obstacle getObstacle() { return obstacle; }

    public void setRunways(Runway[] _runways) { runways = _runways; }
    public Runway[] getRunways() { return runways; }
}
