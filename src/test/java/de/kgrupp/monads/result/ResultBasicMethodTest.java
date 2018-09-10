package de.kgrupp.monads.result;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static de.kgrupp.monads.result.AbstractResultExamples.EMPTY_SUCCESS;
import static de.kgrupp.monads.result.AbstractResultExamples.ERROR_MESSAGE;
import static de.kgrupp.monads.result.AbstractResultExamples.EXCEPTION;
import static de.kgrupp.monads.result.AbstractResultExamples.FAILURE;
import static de.kgrupp.monads.result.AbstractResultExamples.INTERNAL_FAILURE;
import static de.kgrupp.monads.result.AbstractResultExamples.NOT_VALID;
import static de.kgrupp.monads.result.AbstractResultExamples.RESULT_OBJECT;
import static de.kgrupp.monads.result.AbstractResultExamples.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultBasicMethodTest {

    @Nested
    class IsError {

        @Test
        void failure() {
            assertTrue(FAILURE.isError());
        }

        @Test
        void internalFailure() {
            assertTrue(INTERNAL_FAILURE.isError());
        }

        @Test
        void success() {
            assertFalse(SUCCESS.isError());
        }

        @Test
        void emptySuccess() {
            assertFalse(EMPTY_SUCCESS.isError());
        }

    }

    @Nested
    class IsInternalError {

        @Test
        void failure() {
            assertFalse(FAILURE.isInternalError());
        }

        @Test
        void internalFailure() {
            assertTrue(INTERNAL_FAILURE.isInternalError());
        }

        @Test
        void success() {
            assertFalse(SUCCESS.isInternalError());
        }

        @Test
        void emptySuccess() {
            assertFalse(EMPTY_SUCCESS.isInternalError());
        }

    }

    @Nested
    class IsSuccess {

        @Test
        void failure() {
            assertFalse(FAILURE.isSuccess());
        }

        @Test
        void internalFailure() {
            assertFalse(INTERNAL_FAILURE.isSuccess());
        }

        @Test
        void success() {
            assertTrue(SUCCESS.isSuccess());
        }

        @Test
        void emptySuccess() {
            assertTrue(EMPTY_SUCCESS.isSuccess());
        }
    }

    @Nested
    class GetObject {
        @Test
        void failsOnFailure() {
            assertThrows(UnsupportedOperationException.class, FAILURE::getObject);
        }

        @Test
        void failsOnInternalFailure() {
            assertThrows(UnsupportedOperationException.class, INTERNAL_FAILURE::getObject);
        }
    }

    @Nested
    class AsOptional {
        @Test
        void failure() {
            assertFalse(FAILURE.asOptional().isPresent());
        }

        @Test
        void internalFailure() {
            assertFalse(INTERNAL_FAILURE.asOptional().isPresent());
        }

        @Test
        void success() {
            assertTrue(SUCCESS.asOptional().isPresent());
        }

        @Test
        void emptySuccess() {
            assertFalse(EMPTY_SUCCESS.asOptional().isPresent());
        }
    }

    @Nested
    class GetErrorMessage {
        @Test
        void failure() {
            assertEquals(ERROR_MESSAGE, FAILURE.getErrorMessage());
        }

        @Test
        void internalFailure() {
            assertEquals(Result.INTERNAL_FAILURE, INTERNAL_FAILURE.getErrorMessage());
        }

        @Test
        void successFails() {
            assertThrows(UnsupportedOperationException.class, SUCCESS::getErrorMessage);
        }
    }

    @Nested
    class GetThrowable {

        @Test
        void internalFailure() {
            assertEquals(EXCEPTION, INTERNAL_FAILURE.getThrowable());
        }

        @Test
        void successFails() {
            assertThrows(UnsupportedOperationException.class, EMPTY_SUCCESS::getThrowable);
        }

        @Test
        void failureFails() {
            assertThrows(UnsupportedOperationException.class, FAILURE::getThrowable);
        }

    }

    @Nested
    class OfOptional {
        @Test
        void success() {
            Result<String> result = Result.of(Optional.of(RESULT_OBJECT), ERROR_MESSAGE);
            assertTrue(result.isSuccess());
            assertEquals(RESULT_OBJECT, result.getObject());
        }

        @Test
        void failure() {
            Result<String> result = Result.of(Optional.empty(), ERROR_MESSAGE);
            assertTrue(result.isError());
            assertFalse(result.isInternalError());
        }

        @Test
        void internalFailure() {
            Result<String> result = Result.of(Optional.empty(), () -> EXCEPTION);
            assertTrue(result.isError());
            assertTrue(result.isInternalError());
        }
    }

    @Nested
    class OfNullable {
        @Test
        void success() {
            Result<String> result = Result.ofNullable(RESULT_OBJECT, ERROR_MESSAGE);
            assertTrue(result.isSuccess());
            assertEquals(RESULT_OBJECT, result.getObject());
        }

        @Test
        void failure() {
            Result<String> result = Result.ofNullable(null, ERROR_MESSAGE);
            assertTrue(result.isError());
            assertFalse(result.isInternalError());
        }
    }

    @Nested
    class OrElse {
        @Test
        void success() {
            assertEquals(RESULT_OBJECT, SUCCESS.orElse("OTHER"));
        }

        @Test
        void failure() {
            assertEquals(RESULT_OBJECT, FAILURE.orElse(RESULT_OBJECT));
        }

        @Test
        void internalFailure() {
            assertEquals(RESULT_OBJECT, INTERNAL_FAILURE.orElse(RESULT_OBJECT));
        }
    }

    @Nested
    class OrElseGet {
        @Test
        void success() {
            assertEquals(RESULT_OBJECT, SUCCESS.orElseGet(() -> "OTHER"));
        }

        @Test
        void failure() {
            assertEquals(RESULT_OBJECT, FAILURE.orElseGet(() -> RESULT_OBJECT));
        }

        @Test
        void internalFailure() {
            assertEquals(RESULT_OBJECT, INTERNAL_FAILURE.orElseGet(() -> RESULT_OBJECT));
        }
    }

    @Nested
    class GetElseThrow {
        @Test
        void success() {
            assertEquals(RESULT_OBJECT, SUCCESS.orElseThrow());
        }

        @Test
        void failure() {
            ResultException resultException = assertThrows(ResultException.class, FAILURE::orElseThrow);
            assertNull(resultException.getCause());
        }

        @Test
        void internalFailure() {
            ResultException resultException = assertThrows(ResultException.class, INTERNAL_FAILURE::orElseThrow);
            assertNotNull(resultException.getCause());
        }
    }

    @Nested
    class GetElseThrowSupplier {

        @Test
        void success() {
            assertEquals(RESULT_OBJECT, SUCCESS.orElseThrow(success -> {
                Result.fail("This should not be called");
                return null;
            }));
        }

        @Test
        void failure() {
            IllegalArgumentException resultException = assertThrows(IllegalArgumentException.class,
                    () -> FAILURE.orElseThrow(failure -> new IllegalArgumentException(NOT_VALID)));
            assertEquals(NOT_VALID, resultException.getMessage());
            assertNull(resultException.getCause());
        }

        @Test
        void internalFailure() {
            IllegalArgumentException resultException = assertThrows(IllegalArgumentException.class,
                    () -> INTERNAL_FAILURE.orElseThrow(failure -> new IllegalArgumentException(NOT_VALID, failure.getThrowable())));
            assertEquals(NOT_VALID, resultException.getMessage());
            assertNotNull(resultException.getCause());
        }
    }

    @Nested
    class Equals {
        @Test
        void success() {
            assertEquals(Result.of(RESULT_OBJECT), Result.of(RESULT_OBJECT));
        }

        @Test
        void successNotEqual() {
            assertNotEquals(Result.of(RESULT_OBJECT), Result.of(RESULT_OBJECT + "-NOT-EQUAL"));
        }

        @Test
        void failure() {
            assertEquals(Result.fail(ERROR_MESSAGE), Result.fail(ERROR_MESSAGE));
        }

        @Test
        void failureNotEqual() {
            assertNotEquals(Result.fail(ERROR_MESSAGE), Result.fail(ERROR_MESSAGE + "-NOT-EQUAL"));
        }

        @Test
        void internalFailure() {
            assertEquals(Result.fail(EXCEPTION), Result.fail(EXCEPTION));
        }

        @Test
        void internalFailureNotEqual() {
            assertNotEquals(Result.fail(EXCEPTION), Result.fail(new RuntimeException(ERROR_MESSAGE)));
        }
    }

    @Nested
    class HashCode {
        @Test
        void success() {
            assertEquals(Result.of(RESULT_OBJECT).hashCode(), Result.of(RESULT_OBJECT).hashCode());
        }

        @Test
        void successNotEqual() {
            assertNotEquals(Result.of(RESULT_OBJECT).hashCode(), Result.of(RESULT_OBJECT + "-NOT-EQUAL").hashCode());
        }

        @Test
        void failure() {
            assertEquals(Result.fail(ERROR_MESSAGE).hashCode(), Result.fail(ERROR_MESSAGE).hashCode());
        }

        @Test
        void failureNotEqual() {
            assertNotEquals(Result.fail(ERROR_MESSAGE).hashCode(), Result.fail(ERROR_MESSAGE + "-NOT-EQUAL").hashCode());
        }

        @Test
        void internalFailure() {
            assertEquals(Result.fail(EXCEPTION).hashCode(), Result.fail(EXCEPTION).hashCode());
        }

        @Test
        void internalFailureNotEqual() {
            assertNotEquals(Result.fail(EXCEPTION).hashCode(), Result.fail(new RuntimeException(ERROR_MESSAGE)).hashCode());
        }
    }
}