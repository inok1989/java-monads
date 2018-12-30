package de.kgrupp.monads.result;

import java.util.Objects;

/**
 * @author Konstantin Grupp
 */
public class InternalFailure<T> extends Failure<T> {

    private final Throwable throwable;

    InternalFailure(String errorMessage, Throwable throwable) {
        super(errorMessage);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InternalFailure<?> that = (InternalFailure<?>) o;
        return Objects.equals(throwable, that.throwable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), throwable);
    }

    @Override
    public String toString() {
        return "InternalFailure{" + throwable + '}';
    }
}
