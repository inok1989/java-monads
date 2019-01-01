package de.kgrupp.monads.result;

import java.util.Objects;

/**
 * @author Konstantin Grupp
 */
public class InternalFailure<T> extends Failure<T> {

    private final Exception exception;

    InternalFailure(String errorMessage, Exception exception) {
        super(errorMessage);
        this.exception = exception;
    }

    @Override
    public boolean isInternalError() {
        return true;
    }

    @Override
    public T getObject() {
        throw new UnsupportedOperationException(Helper.methodNotSupportedMessageBuilder(this), exception);
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InternalFailure<?> that = (InternalFailure<?>) o;
        return Objects.equals(exception, that.exception);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), exception);
    }

    @Override
    public String toString() {
        return "InternalFailure{" + exception + '}';
    }
}
