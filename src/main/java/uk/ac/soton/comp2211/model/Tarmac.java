package uk.ac.soton.comp2211.model;

public class Tarmac {
    private int id;
    private int length;
    private Obstacle obstacle;

    public Tarmac(int _id, Obstacle _obstacle) {
        id = _id;
        obstacle = _obstacle;
    }

    public int getID() { return id; }
    public int getLength() { return length; }

    public void setObstacle(Obstacle _obstacle) { obstacle = _obstacle; }
    public void removeObstacle(int _index) { obstacle = null; }
    public Obstacle getObstacle() { return obstacle; }
}
