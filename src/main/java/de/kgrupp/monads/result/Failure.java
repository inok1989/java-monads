package de.kgrupp.monads.result;

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
        throw new UnsupportedOperationException(Helper.THIS_METHOD_IS_NOT_SUPPORTED);
    }

    @Override
    public String getSuccessMessage() {
        throw new UnsupportedOperationException(Helper.THIS_METHOD_IS_NOT_SUPPORTED);
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public Throwable getThrowable() {
        throw new UnsupportedOperationException(Helper.THIS_METHOD_IS_NOT_SUPPORTED);
    }
}
