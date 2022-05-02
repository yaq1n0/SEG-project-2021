package uk.ac.soton.comp2211.exceptions;

import org.apache.logging.log4j.Logger;

/**
 * Thrown when an invalid size parameter is assigned to an obstacle.
 */
public class SizeException extends ProgramException {
    public SizeException(Logger _logger, String _message) { 
        super(_logger, _message);
    }
}
