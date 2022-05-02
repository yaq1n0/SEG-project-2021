package uk.ac.soton.comp2211.exceptions;

import org.apache.logging.log4j.Logger;

public class LoadingException extends ProgramException {
    public LoadingException(Logger _logger, String _message) { 
        super(_logger, _message);
    }
}
