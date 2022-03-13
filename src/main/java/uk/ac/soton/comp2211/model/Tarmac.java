package uk.ac.soton.comp2211.model;

public class Tarmac {
    private int id;
    private int length;
    private Obstacle obstacle;

    private Runway[] runways;

    public Tarmac(int _id) { id = _id; }

    public int getID() { return id; }
    public int getLength() { return length; }

    public void setObstacle(Obstacle _obstacle) { obstacle = _obstacle; }
    public void removeObstacle() { obstacle = null; }
    public Obstacle getObstacle() { return obstacle; }

    public void setRunways(Runway[] _runways) { runways = _runways; }
    public Runway[] getRunways() { return runways; }
}
