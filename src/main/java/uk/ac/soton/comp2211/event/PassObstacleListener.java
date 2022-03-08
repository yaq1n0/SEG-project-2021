package uk.ac.soton.comp2211.event;

import uk.ac.soton.comp2211.model.Obstacle;

/**
 * Handle passing the user's choice of obstacle back to the controller.
 */
public interface PassObstacleListener {

    void passAirport(Obstacle obstacle);
    
}
