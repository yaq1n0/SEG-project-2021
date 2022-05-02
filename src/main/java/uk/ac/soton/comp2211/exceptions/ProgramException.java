package uk.ac.soton.comp2211.exceptions;

import org.apache.logging.log4j.Logger;

public class ProgramException extends Exception {
    public ProgramException(Logger _logger, String _message) {
        super(_message);

        if (_logger == null) return;

        _logger.error(_message);
    }
}
