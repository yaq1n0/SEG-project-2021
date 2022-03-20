package uk.ac.soton.comp2211.model;

import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.exceptions.SizeException;

public class Obstacle {
    private String name;

    private int length; // Length parallel to the runway.
    private int width; // Length perpendicular to the runway.
    private int height;

    private Position position; // Posistion of obstacle from centre of runway.

    public Obstacle(String _name, int _length, int _width, int _height) throws SizeException {
        name = _name;

        if (_length >= 0) length = _length;
        else throw new SizeException("Length must be positive!");

        if (_width >= 0) width = _width;
        else throw new SizeException("Width must be positive!");

        if (_height >= 0) height = _height;
        else throw new SizeException("Height must be positive!");
    }

    public String getName() { return name; }

    public int getLength() { return length; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setPosition(Position _position) { position = _position; }

    public boolean hasPosition() {
        if (this.position==null) {
            return false;
        } else {
            return true;
        }
    }
    
    public Position getPosition() throws PositionException { 
        if (position == null) throw new PositionException("Position of obstacle not set!");

        return position; 
    }
}
