package uk.ac.soton.comp2211.exceptions;

import org.apache.logging.log4j.Logger;

/**
 * Thrown when an XML file doesn't match the schema.
 */
public class SchemaException extends ProgramException {
    public SchemaException(Logger _logger, String _message) { 
        super(_logger, _message); 
    }
}
