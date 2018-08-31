package de.kgrupp.monads.result;


/**
 * @author Konstantin Grupp
 */
final class Helper {

    static final String THIS_METHOD_IS_NOT_SUPPORTED = "This method is not supported.";
    private static final String SUCCESS_CAN_NOT_BE_CONVERTED = "Success can not be converted.";

    private Helper() {
        // utility class
    }

    static <U, T> Result<U> transform(Result<T> result) {
        if (result.isInternalError()) {
            InternalFailure internalFailure = (InternalFailure) result;
            return Result.fail(internalFailure.getThrowable());
        } else if (result.isError()) {
            return Result.fail(result.getErrorMessage());
        } else {
            throw new IllegalArgumentException(SUCCESS_CAN_NOT_BE_CONVERTED);
        }
    }

    static ResultException toException(Result<?> result) {
        if (result.isInternalError()) {
            return new ResultException(result.getThrowable());
        } else if (result.isError()) {
            return new ResultException(result.getErrorMessage());
        } else {
            throw new IllegalArgumentException("A ResultException can not be build with a Success");
        }
    }
}
