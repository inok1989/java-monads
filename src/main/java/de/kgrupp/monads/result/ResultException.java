package de.kgrupp.monads.result;

/**
 * @author Konstantin Grupp
 */
public class ResultException extends RuntimeException {

    ResultException(String message) {
        super(message);
    }

    ResultException(String message, Exception exception) {
        super(message, exception);
    }
}
