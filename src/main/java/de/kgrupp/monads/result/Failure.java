package de.kgrupp.monads.result;

import java.util.Objects;

/**
 * @author Konstantin Grupp
 */
public class Failure<T> implements Result<T> {

    private final String errorMessage;

    Failure(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public boolean isInternalError() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T getObject() {
        throw new UnsupportedOperationException(Helper.methodNotSupportedMessageBuilder(this));
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public Exception getException() {
        throw new UnsupportedOperationException(Helper.methodNotSupportedMessageBuilder(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Failure<?> failure = (Failure<?>) o;
        return Objects.equals(errorMessage, failure.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage);
    }

    @Override
    public String toString() {
        return "Failure{" + errorMessage + '}';
    }
}
