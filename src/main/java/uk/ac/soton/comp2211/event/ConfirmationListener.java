package uk.ac.soton.comp2211.event;

/**
 * Listener to confirm the choice to execute some behaviour.
 */
public interface ConfirmationListener {
    
    void confirmed(boolean result);
    
}
