package de.kgrupp.monads.result;

import java.util.Objects;

/**
 * @author Konstantin Grupp
 */
public class Success<T> implements Result<T> {

    private final String successMessage;
    private final T object;

    Success(String successMessage, T object) {
        this.successMessage = successMessage;
        this.object = object;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public boolean isInternalError() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public String getSuccessMessage() {
        return successMessage;
    }

    @Override
    public String getErrorMessage() {
        throw new UnsupportedOperationException(Helper.THIS_METHOD_IS_NOT_SUPPORTED);
    }

    @Override
    public Throwable getThrowable() {
        throw new UnsupportedOperationException(Helper.THIS_METHOD_IS_NOT_SUPPORTED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Success<?> success = (Success<?>) o;
        return Objects.equals(successMessage, success.successMessage) &&
                Objects.equals(object, success.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(successMessage, object);
    }
}
