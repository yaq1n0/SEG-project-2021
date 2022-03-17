package uk.ac.soton.comp2211.model;

import uk.ac.soton.comp2211.exceptions.PositionException;

public class Position {
    private int distanceFromWest; // Horizonyal distance from the west end of the runway.
    private int distanceFromEast; // Horizontal distance from the east end of the runway.
    private int centreLineDisplacement; // Vertical displacement of object from the centre line of the runway.

    public Position(int _distanceFromWest, 
                    int _distanceFromEast, 
                    int _centreLineDisplacement) throws PositionException {
        setDistanceFromWest(_distanceFromWest);
        setDistanceFromEast(_distanceFromEast);

        centreLineDisplacement = _centreLineDisplacement;
    }

    public void setDistanceFromWest(int _distanceFromWest) throws PositionException {
        if (_distanceFromWest >= 0) distanceFromWest = _distanceFromWest;
        else throw new PositionException("Distance from west must be positive!");
    }

    public void setDistanceFromEast(int _distanceFromEast) throws PositionException {
        if (_distanceFromEast >= 0) distanceFromEast = _distanceFromEast;
        else throw new PositionException("Distance from east must be positive!");
    }

    public void setCentreLineDisplacement(int _centreLineDisplacement) { 
        centreLineDisplacement = _centreLineDisplacement; 
    }

    public int getDistanceFromWest() { return distanceFromWest; }
    public int getDistanceFromEast() { return distanceFromEast; }
    public int getCentreLineDisplacement() { return centreLineDisplacement; }
}