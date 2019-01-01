package de.kgrupp.monads.result;


/**
 * @author Konstantin Grupp
 */
final class Helper {

    static final String OPTIONAL_IS_EMPTY = "Optional is empty.";
    private static final String SUCCESS_CAN_NOT_BE_CONVERTED = "Success can not be converted to another type.";

    private Helper() {
        // utility class
    }

    static String methodNotSupportedMessageBuilder(Result<?> result) {
        return String.format("This method is not supported result is %s(%s)", result.getClass().getName(), result.isError() ? result.getErrorMessage() : "");
    }

    static <U, T> Result<U> transform(Result<T> result) {
        if (result.isSuccess()) {
            throw new IllegalArgumentException(SUCCESS_CAN_NOT_BE_CONVERTED);
        } else if (result.isInternalError()) {
            return Result.fail(result.getErrorMessage(), result.getException());
        } else {
            return Result.fail(result.getErrorMessage());
        }
    }

    static ResultException toException(Result<?> result) {
        if (result.isSuccess()) {
            throw new IllegalArgumentException("A ResultException can not be build with a Success");
        } else if (result.isInternalError()) {
            return new ResultException(result.getErrorMessage(), result.getException());
        } else {
            return new ResultException(result.getErrorMessage());
        }
    }
}
