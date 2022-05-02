package uk.ac.soton.comp2211.exceptions;

import org.apache.logging.log4j.Logger;

/**
 * Thrown by the system model when it fails to write data to an XML file.
 */
public class WritingException extends ProgramException {
    public WritingException(Logger _logger, String _message) { 
        super(_logger, _message); 
    }
}