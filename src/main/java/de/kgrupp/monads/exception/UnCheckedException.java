package de.kgrupp.monads.exception;

/**
 * @author Konstantin Grupp
 */
public class UnCheckedException extends RuntimeException {
    public UnCheckedException(Throwable throwable) {
        super(throwable);
    }

    public UnCheckedException(String message) {
        super(message);
    }

    public UnCheckedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
