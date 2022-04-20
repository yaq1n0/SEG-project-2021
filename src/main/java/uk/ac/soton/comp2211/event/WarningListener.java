package uk.ac.soton.comp2211.event;

public interface WarningListener {
    
    void openDialog(String[] messages, ConfirmationListener listener);
    
}
