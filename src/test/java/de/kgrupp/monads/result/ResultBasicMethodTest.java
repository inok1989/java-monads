package de.kgrupp.monads.result;

import de.kgrupp.monads.exception.UnCheckedException;
import org.junit.Test;

import java.util.Optional;

import static de.kgrupp.monads.result.AbstractResultTest.ERROR_MESSAGE;
import static de.kgrupp.monads.result.AbstractResultTest.EXCEPTION;
import static de.kgrupp.monads.result.AbstractResultTest.RESULT_OBJECT;
import static de.kgrupp.monads.result.AbstractResultTest.SUCCESS_MESSAGE;
import static de.kgrupp.monads.result.AbstractResultTest.EMPTY_SUCCESS;
import static de.kgrupp.monads.result.AbstractResultTest.FAILURE;
import static de.kgrupp.monads.result.AbstractResultTest.INTERNAL_FAILURE;
import static de.kgrupp.monads.result.AbstractResultTest.SUCCESS;
import static de.kgrupp.monads.result.AbstractResultTest.SUCCESS_WITH_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ResultBasicMethodTest {

    @Test
    public void testIsError() {
        assertTrue(FAILURE.isError());
        assertTrue(INTERNAL_FAILURE.isError());
        assertFalse(SUCCESS.isError());
        assertFalse(SUCCESS_WITH_MESSAGE.isError());
        assertFalse(EMPTY_SUCCESS.isError());
    }

    @Test
    public void testIsInternalError() {
        assertFalse(FAILURE.isInternalError());
        assertTrue(INTERNAL_FAILURE.isInternalError());
        assertFalse(SUCCESS.isInternalError());
        assertFalse(SUCCESS_WITH_MESSAGE.isInternalError());
        assertFalse(EMPTY_SUCCESS.isInternalError());
    }

    @Test
    public void testIsSuccess() {
        assertFalse(FAILURE.isSuccess());
        assertFalse(INTERNAL_FAILURE.isSuccess());
        assertTrue(SUCCESS.isSuccess());
        assertTrue(SUCCESS_WITH_MESSAGE.isSuccess());
        assertTrue(EMPTY_SUCCESS.isSuccess());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetObjectFailsOnFailure() {
        FAILURE.getObject();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetObjectFailsOnInternalFailure() {
        INTERNAL_FAILURE.getObject();
    }

    @Test
    public void testAsOptional() {
        assertFalse(FAILURE.asOptional().isPresent());
        assertFalse(INTERNAL_FAILURE.asOptional().isPresent());
        assertTrue(SUCCESS.asOptional().isPresent());
        assertTrue(SUCCESS_WITH_MESSAGE.asOptional().isPresent());
        assertFalse(EMPTY_SUCCESS.asOptional().isPresent());
    }

    @Test
    public void testGetSuccessMessage() {
        assertEquals(Result.SUCCESS, SUCCESS.getSuccessMessage());
        assertEquals(SUCCESS_MESSAGE, SUCCESS_WITH_MESSAGE.getSuccessMessage());
        assertEquals(SUCCESS_MESSAGE, EMPTY_SUCCESS.getSuccessMessage());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSuccessMessageFails() {
        FAILURE.getSuccessMessage();
    }

    @Test
    public void testGetErrorMessage() {
        assertEquals(ERROR_MESSAGE, FAILURE.getErrorMessage());
        assertEquals(Result.INTERNAL_FAILURE, INTERNAL_FAILURE.getErrorMessage());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetErrorMessageFails() {
        SUCCESS.getErrorMessage();
    }

    @Test
    public void testGetThrowable() {
        assertEquals(EXCEPTION, INTERNAL_FAILURE.getThrowable());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetThrowableFailsForSuccess() {
        assertNotNull(EMPTY_SUCCESS.getThrowable());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetThrowableFailsForFailure() {
        assertNotNull(FAILURE.getThrowable());
    }

    @Test
    public void testOfOptionalSuccess() {
        Result<String> result = Result.of(Optional.of(RESULT_OBJECT), ERROR_MESSAGE);
        assertTrue(result.isSuccess());
        assertEquals(RESULT_OBJECT, result.getObject());
    }

    @Test
    public void testOfOptionalError() {
        Result<String> result = Result.of(Optional.empty(), ERROR_MESSAGE);
        assertTrue(result.isError());
        assertFalse(result.isInternalError());
    }

    @Test
    public void testOfOptionalInternalError() {
        Result<String> result = Result.of(Optional.empty(), () -> EXCEPTION);
        assertTrue(result.isError());
        assertTrue(result.isInternalError());
    }

    @Test
    public void testOfNullableSuccess() {
        Result<String> result = Result.ofNullable(RESULT_OBJECT, ERROR_MESSAGE);
        assertTrue(result.isSuccess());
        assertEquals(RESULT_OBJECT, result.getObject());
    }

    @Test
    public void testOfNullableError() {
        Result<String> result = Result.ofNullable(null, ERROR_MESSAGE);
        assertTrue(result.isError());
        assertFalse(result.isInternalError());
    }

    @Test
    public void testOrElse() {
        assertEquals(RESULT_OBJECT, SUCCESS.orElse("OTHER"));
        assertEquals(RESULT_OBJECT, FAILURE.orElse(RESULT_OBJECT));
        assertEquals(RESULT_OBJECT, INTERNAL_FAILURE.orElse(RESULT_OBJECT));
    }

    @Test
    public void testOrElseGet() {
        assertEquals(RESULT_OBJECT, SUCCESS.orElseGet(() -> "OTHER"));
        assertEquals(RESULT_OBJECT, FAILURE.orElseGet(() -> RESULT_OBJECT));
        assertEquals(RESULT_OBJECT, INTERNAL_FAILURE.orElseGet(() -> RESULT_OBJECT));
    }

    @Test
    public void getElseThrow() {
        assertEquals(RESULT_OBJECT, SUCCESS.orElseThrow());
    }

    @Test(expected = UnCheckedException.class)
    public void getElseThrowFailure() {
        FAILURE.orElseThrow();
    }

    @Test(expected = UnCheckedException.class)
    public void getElseThrowInternalFailure() {
        INTERNAL_FAILURE.orElseThrow();
    }
}