package uk.ac.soton.comp2211.event;

import uk.ac.soton.comp2211.model.Runway;

/**
 * Listener for when the user tries to delete a tarmac from an airport
 */
public interface DeleteTarmacListener {
    
    void attemptDeletion(Runway runway);
    
}
