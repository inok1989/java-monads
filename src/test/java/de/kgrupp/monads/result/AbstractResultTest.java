package de.kgrupp.monads.result;

final class AbstractResultTest {

    private AbstractResultTest() {
        // constants
    }

    static final String SUCCESS_MESSAGE = "We did it";
    static final String ERROR_MESSAGE = "We failed";
    static final String RESULT_OBJECT = "This is our result";
    static final Throwable EXCEPTION = new RuntimeException(ERROR_MESSAGE);

    static final Result<String> FAILURE = Result.fail(ERROR_MESSAGE);
    static final Result<String> INTERNAL_FAILURE = Result.fail(EXCEPTION);
    static final Result<String> SUCCESS = Result.of(RESULT_OBJECT);
    static final Result<String> SUCCESS_WITH_MESSAGE = Result.of(SUCCESS_MESSAGE, RESULT_OBJECT);
    static final Result<Void> EMPTY_SUCCESS = Result.emptySuccess(SUCCESS_MESSAGE);

}