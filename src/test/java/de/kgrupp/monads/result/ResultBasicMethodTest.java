package de.kgrupp.monads.result;

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
import static de.kgrupp.monads.result.Result.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultBasicMethodTest {

    @Test
    void testIsError() {
        assertTrue(FAILURE.isError());
        assertTrue(INTERNAL_FAILURE.isError());
        assertFalse(SUCCESS.isError());
        assertFalse(EMPTY_SUCCESS.isError());
    }

    @Test
    void testIsInternalError() {
        assertFalse(FAILURE.isInternalError());
        assertTrue(INTERNAL_FAILURE.isInternalError());
        assertFalse(SUCCESS.isInternalError());
        assertFalse(EMPTY_SUCCESS.isInternalError());
    }

    @Test
    void testIsSuccess() {
        assertFalse(FAILURE.isSuccess());
        assertFalse(INTERNAL_FAILURE.isSuccess());
        assertTrue(SUCCESS.isSuccess());
        assertTrue(EMPTY_SUCCESS.isSuccess());
    }

    @Test
    void testGetObjectFailsOnFailure() {
        assertThrows(UnsupportedOperationException.class, FAILURE::getObject);
    }

    @Test
    void testGetObjectFailsOnInternalFailure() {
        assertThrows(UnsupportedOperationException.class, INTERNAL_FAILURE::getObject);
    }

    @Test
    void testAsOptional() {
        assertFalse(FAILURE.asOptional().isPresent());
        assertFalse(INTERNAL_FAILURE.asOptional().isPresent());
        assertTrue(SUCCESS.asOptional().isPresent());
        assertFalse(EMPTY_SUCCESS.asOptional().isPresent());
    }

    @Test
    void testGetErrorMessage() {
        assertEquals(ERROR_MESSAGE, FAILURE.getErrorMessage());
        assertEquals(Result.INTERNAL_FAILURE, INTERNAL_FAILURE.getErrorMessage());
    }

    @Test
    void testGetErrorMessageFails() {
        assertThrows(UnsupportedOperationException.class, SUCCESS::getErrorMessage);
    }

    @Test
    void testGetThrowable() {
        assertEquals(EXCEPTION, INTERNAL_FAILURE.getThrowable());
    }

    @Test
    void testGetThrowableFailsForSuccess() {
        assertThrows(UnsupportedOperationException.class, EMPTY_SUCCESS::getThrowable);
    }

    @Test
    void testGetThrowableFailsForFailure() {
        assertThrows(UnsupportedOperationException.class, FAILURE::getThrowable);
    }

    @Test
    void testOfOptionalSuccess() {
        Result<String> result = Result.of(Optional.of(RESULT_OBJECT), ERROR_MESSAGE);
        assertTrue(result.isSuccess());
        assertEquals(RESULT_OBJECT, result.getObject());
    }

    @Test
    void testOfOptionalError() {
        Result<String> result = Result.of(Optional.empty(), ERROR_MESSAGE);
        assertTrue(result.isError());
        assertFalse(result.isInternalError());
    }

    @Test
    void testOfOptionalInternalError() {
        Result<String> result = Result.of(Optional.empty(), () -> EXCEPTION);
        assertTrue(result.isError());
        assertTrue(result.isInternalError());
    }

    @Test
    void testOfNullableSuccess() {
        Result<String> result = Result.ofNullable(RESULT_OBJECT, ERROR_MESSAGE);
        assertTrue(result.isSuccess());
        assertEquals(RESULT_OBJECT, result.getObject());
    }

    @Test
    void testOfNullableError() {
        Result<String> result = Result.ofNullable(null, ERROR_MESSAGE);
        assertTrue(result.isError());
        assertFalse(result.isInternalError());
    }

    @Test
    void testOrElse() {
        assertEquals(RESULT_OBJECT, SUCCESS.orElse("OTHER"));
        assertEquals(RESULT_OBJECT, FAILURE.orElse(RESULT_OBJECT));
        assertEquals(RESULT_OBJECT, INTERNAL_FAILURE.orElse(RESULT_OBJECT));
    }

    @Test
    void testOrElseGet() {
        assertEquals(RESULT_OBJECT, SUCCESS.orElseGet(() -> "OTHER"));
        assertEquals(RESULT_OBJECT, FAILURE.orElseGet(() -> RESULT_OBJECT));
        assertEquals(RESULT_OBJECT, INTERNAL_FAILURE.orElseGet(() -> RESULT_OBJECT));
    }

    @Test
    void getElseThrow() {
        assertEquals(RESULT_OBJECT, SUCCESS.orElseThrow());
    }

    @Test
    void getElseThrowFailure() {
        ResultException resultException = assertThrows(ResultException.class, FAILURE::orElseThrow);
        assertNull(resultException.getCause());
    }

    @Test
    void getElseThrowInternalFailure() {
        ResultException resultException = assertThrows(ResultException.class, INTERNAL_FAILURE::orElseThrow);
        assertNotNull(resultException.getCause());
    }

    @Test
    void getElseThrowSupplier() {
        assertEquals(RESULT_OBJECT, SUCCESS.orElseThrow(success -> {
            fail("This should not be called");
            return null;
        }));
    }

    @Test
    void getElseThrowSupplierFailure() {
        IllegalArgumentException resultException = assertThrows(IllegalArgumentException.class,
                () -> FAILURE.orElseThrow(failure -> new IllegalArgumentException(NOT_VALID)));
        assertEquals(NOT_VALID, resultException.getMessage());
        assertNull(resultException.getCause());
    }

    @Test
    void getElseThrowSupplierInternalFailure() {
        IllegalArgumentException resultException = assertThrows(IllegalArgumentException.class,
                () -> INTERNAL_FAILURE.orElseThrow(failure -> new IllegalArgumentException(NOT_VALID, failure.getThrowable())));
        assertEquals(NOT_VALID, resultException.getMessage());
        assertNotNull(resultException.getCause());
    }

    @Test
    void equalsMethods() {
        assertEquals(Result.of(RESULT_OBJECT), Result.of(RESULT_OBJECT));
        assertNotEquals(Result.of(RESULT_OBJECT), Result.of(RESULT_OBJECT + "-NOT-EQUAL"));
        assertEquals(fail(ERROR_MESSAGE), fail(ERROR_MESSAGE));
        assertNotEquals(fail(ERROR_MESSAGE), fail(ERROR_MESSAGE + "-NOT-EQUAL"));
        assertEquals(fail(EXCEPTION), fail(EXCEPTION));
        assertNotEquals(fail(EXCEPTION), fail(new RuntimeException(ERROR_MESSAGE)));
    }

    @Test
    void hashCodeMethods() {
        assertEquals(Result.of(RESULT_OBJECT).hashCode(), Result.of(RESULT_OBJECT).hashCode());
        assertNotEquals(Result.of(RESULT_OBJECT).hashCode(), Result.of(RESULT_OBJECT + "-NOT-EQUAL").hashCode());
        assertEquals(fail(ERROR_MESSAGE).hashCode(), fail(ERROR_MESSAGE).hashCode());
        assertNotEquals(fail(ERROR_MESSAGE).hashCode(), fail(ERROR_MESSAGE + "-NOT-EQUAL").hashCode());
        assertEquals(fail(EXCEPTION).hashCode(), fail(EXCEPTION).hashCode());
        assertNotEquals(fail(EXCEPTION).hashCode(), fail(new RuntimeException(ERROR_MESSAGE)).hashCode());
    }
}