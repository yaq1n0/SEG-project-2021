package uk.ac.soton.comp2211.model;

import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.exceptions.SizeException;

public class Obstacle {
    private String name;

    private int length; // Length parallel to the runway, used for visualization
    private int width; // Width perpendicular to the runway, used for visualization
    private int height; // Height perpendicular to the runway, used in redeclaration calculations

    private Position position; // Position of obstacle as distance from thresholds

    public Obstacle(String _name, int _length, int _width, int _height) throws SizeException {
        name = _name;

        if (_length >= 0) length = _length;
        else throw new SizeException(null, "Length must be positive!");

        if (_width >= 0) width = _width;
        else throw new SizeException(null, "Width must be positive!");

        if (_height >= 0) height = _height;
        else throw new SizeException(null, "Height must be positive!");

        position = null;
    }

    public String getName() { return name; }
    public int getLength() { return length; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean hasValidPosition() {
        return this.position != null;
    }
    public Position getPosition() throws PositionException {
        if (position == null) throw new PositionException(null, "Position of obstacle not set!");

        return position;
    }

    public void setPosition(Position _position) { position = _position; }
}
