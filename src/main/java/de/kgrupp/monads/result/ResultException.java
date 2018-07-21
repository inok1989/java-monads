package de.kgrupp.monads.result;

/**
 * @author Konstantin Grupp
 */
public class ResultException extends RuntimeException {
    public ResultException(Throwable throwable) {
        super(throwable);
    }

    public ResultException(String message) {
        super(message);
    }

    public ResultException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
