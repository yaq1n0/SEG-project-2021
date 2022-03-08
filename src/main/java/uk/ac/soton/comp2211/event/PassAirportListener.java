package uk.ac.soton.comp2211.event;

/**
 * Handle passing the user's choice of airport to display back to the controller.
 */
public interface PassAirportListener {
    
    void passAirport(String airportPath);
    
}
