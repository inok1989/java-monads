package de.kgrupp.monads.result;

/**
 * @author Konstantin Grupp
 */
public class InternalFailure<T> extends Failure<T> {

    private final Throwable throwable;

    InternalFailure(Throwable throwable) {
        super("Internal Failure.");
        this.throwable = throwable;
    }

    @Override
    public boolean isInternalError() {
        return true;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }
}
