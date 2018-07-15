package de.kgrupp.monads.result;

import org.junit.Test;

import java.util.function.Function;

import static de.kgrupp.monads.result.AbstractResultTest.ERROR_MESSAGE;
import static de.kgrupp.monads.result.AbstractResultTest.FAILURE;
import static de.kgrupp.monads.result.AbstractResultTest.INTERNAL_FAILURE;
import static de.kgrupp.monads.result.AbstractResultTest.SUCCESS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResultHigherOrderTest {

    private Function<String, Boolean> map = str -> true;
    private Function<String, Result<Boolean>> flatMap = str -> Result.of(true);

    @Test
    public void testMapSuccess() {
        Result<Boolean> result = SUCCESS.map(map);
        assertTrue(result.isSuccess());
        assertTrue(result.getObject());
    }

    @Test
    public void testMapFailure() {
        Result<Boolean> result = FAILURE.map(map);
        assertTrue(result.isError());
        assertFalse(result.isInternalError());
    }

    @Test
    public void testMapInternalFailure() {
        Result<Boolean> result = INTERNAL_FAILURE.map(map);
        assertTrue(result.isError());
        assertTrue(result.isInternalError());
    }

    @Test
    public void testFlatMapSuccess() {
        Result<Boolean> result = SUCCESS.flatMap(flatMap);
        assertTrue(result.isSuccess());
        assertTrue(result.getObject());
    }

    @Test
    public void testFlatMapSuccessWithMap() {
        Function<String, Result<Boolean>> flatMapFails = str -> Result.fail(ERROR_MESSAGE);
        Result<Boolean> result = SUCCESS.flatMap(flatMapFails);
        assertTrue(result.isError());
    }

    @Test
    public void testFlatMapError() {
        Result<Boolean> result = FAILURE.flatMap(flatMap);
        assertTrue(result.isError());
    }

    @Test
    public void testFilter() {
        Result<String> result = SUCCESS.filter(str -> true, ERROR_MESSAGE);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testFilterToError() {
        Result<String> result = SUCCESS.filter(str -> false, ERROR_MESSAGE);
        assertTrue(result.isError());
    }

}
