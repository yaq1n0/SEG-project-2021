package uk.ac.soton.comp2211.model;

import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.exceptions.SizeException;

public class Position {
    // private boolean bi; // does the position object contain positions for a bidirectional tarmac
    private int distance; // distance from the threshold
    private int distanceAlt; // distance from the threshold of the alternate/reverse runway
    private int centreLineDisplacement; // displacement of object from the centre line of the tarmac.

    public Position(int _distance, int _centreLineDisplacement) throws PositionException, SizeException {
        setDistance(_distance);
        setCentreLineDisplacement(_centreLineDisplacement);
    }

    public Position(int _distance, int _distanceAlt, int _centreLineDisplacement) throws PositionException, SizeException {
        setDistance(_distance);
        setDistanceAlt(_distanceAlt);
        setCentreLineDisplacement(_centreLineDisplacement);
    }


    public void setDistance(int _distance) throws PositionException {
        distance = _distance;
    }

    public void setDistanceAlt(int _distanceAlt) throws PositionException {
        distanceAlt = _distanceAlt;
    }

    // distance can be negative, negative is one direction, positive the other direction
    // we need to know for UI obstacle placement
    public void setCentreLineDisplacement(int _centreLineDisplacement) {
        centreLineDisplacement = _centreLineDisplacement;
    }

    public int getDistance() { return distance; }
    public int getDistanceAlt() { return distanceAlt; }
    public int getCentreLineDisplacement() { return centreLineDisplacement; }
}