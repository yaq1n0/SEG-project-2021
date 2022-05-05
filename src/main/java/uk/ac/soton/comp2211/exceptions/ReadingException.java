package uk.ac.soton.comp2211.exceptions;

import org.apache.logging.log4j.Logger;

/**
 * Thrown by the system model when it fails to read data from an XML file.
 */
public class ReadingException extends ProgramException {
    public ReadingException(Logger _logger, String _message) { super(_logger, _message); }
}
