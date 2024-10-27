package backend.academy.services;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

/**
 * ExceptionHandlerService is a utility class that provides methods for handling exceptions.
 * It uses Log4j2 for logging error messages.
 */
@Log4j2
@UtilityClass
public class ExceptionHandlerService {

    /**
     * Logs the error message of the given exception.
     *
     * @param e the Exception to be handled
     */
    public void handleException(Exception e) {
        log.error(e.getMessage());
    }
}
