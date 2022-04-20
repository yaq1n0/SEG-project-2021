package uk.ac.soton.comp2211.exceptions;

/**
 * Thrown by the system model when it fails to write data to an XML file.
 */
public class WritingException extends Exception {
    public WritingException(String _message) { super(_message); }
}