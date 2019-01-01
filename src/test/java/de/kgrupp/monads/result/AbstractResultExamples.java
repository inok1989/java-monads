package de.kgrupp.monads.result;

final class AbstractResultExamples {

    private AbstractResultExamples() {
        // constants
    }

    static final String ERROR_MESSAGE = "We failed";
    static final String INTERNAL_ERROR_MESSAGE = "We failed";
    static final String RESULT_OBJECT = "This is our result";
    static final String NOT_VALID = "Not valid";
    static final Exception EXCEPTION = new RuntimeException("Some Reason");

    static final Result<String> FAILURE = Result.fail(ERROR_MESSAGE);
    static final Result<String> INTERNAL_FAILURE = Result.fail(INTERNAL_ERROR_MESSAGE, EXCEPTION);
    static final Result<String> SUCCESS = Result.of(RESULT_OBJECT);
    static final Result<Void> EMPTY_SUCCESS = Result.emptySuccess();

}