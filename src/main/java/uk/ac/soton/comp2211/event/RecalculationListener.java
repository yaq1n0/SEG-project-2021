package uk.ac.soton.comp2211.event;

import uk.ac.soton.comp2211.model.Runway;

/**
 * Listen for when the user tries to run a calculation on runway parameters.
 */
public interface RecalculationListener {
    
    void runCalculation(Runway runway);
    
}
