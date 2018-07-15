package de.kgrupp.monads.result;

/**
 * @author Konstantin Grupp
 */
public class Success<T> implements Result<T> {

    private final String successMessage;
    private final T object;

    Success(T object) {
        this.successMessage = null;
        this.object = object;
    }

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
}
