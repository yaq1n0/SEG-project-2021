package uk.ac.soton.comp2211.model;

import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.exceptions.SizeException;

public class Position {
    private boolean bi; // does the position object contain positions for a bidirectional tarmac
    private int distance; // distance from the threshold
    private int distanceAlt; // distance from the threshold of the alternate/reverse runway
    private int centreLineDisplacement; // displacement of object from the centre line of the tarmac.

    public Position(int _distance, int _centreLineDisplacement) throws PositionException, SizeException {
        bi = false;
        setDistance(_distance);
        setCentreLineDisplacement(_centreLineDisplacement);
    }

    public Position(int _distance, int _distanceAlt, int _centreLineDisplacement) throws PositionException, SizeException {
        bi = true;
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

    public void setCentreLineDisplacement(int _centreLineDisplacement) {
        if (_centreLineDisplacement >= 0) centreLineDisplacement = _centreLineDisplacement;
        else throw new SizeException("Distance from centre line must be positive");
    }

    public int getDistance() { return distance; }
    public int getDistanceAlt() { return distanceAlt; }
    public int getCentreLineDisplacement() { return centreLineDisplacement; }
}