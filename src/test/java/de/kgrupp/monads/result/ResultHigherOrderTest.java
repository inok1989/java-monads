package de.kgrupp.monads.result;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static de.kgrupp.monads.result.AbstractResultExamples.ERROR_MESSAGE;
import static de.kgrupp.monads.result.AbstractResultExamples.FAILURE;
import static de.kgrupp.monads.result.AbstractResultExamples.INTERNAL_FAILURE;
import static de.kgrupp.monads.result.AbstractResultExamples.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultHigherOrderTest {

    private Function<String, Boolean> map = str -> true;
    private Function<String, Result<Boolean>> flatMap = str -> Result.of(true);

    @Test
    void testMapSuccess() {
        Result<Boolean> result = SUCCESS.map(map);
        assertTrue(result.isSuccess());
        assertTrue(result.getObject());
    }

    @Test
    void testMapFailure() {
        Result<Boolean> result = FAILURE.map(map);
        assertTrue(result.isError());
        assertFalse(result.isInternalError());
    }

    @Test
    void testMapInternalFailure() {
        Result<Boolean> result = INTERNAL_FAILURE.map(map);
        assertTrue(result.isError());
        assertTrue(result.isInternalError());
    }

    @Test
    void testFlatMapSuccess() {
        Result<Boolean> result = SUCCESS.flatMap(flatMap);
        assertTrue(result.isSuccess());
        assertTrue(result.getObject());
    }

    @Test
    void testFlatMapSuccessWithMap() {
        Function<String, Result<Boolean>> flatMapFails = str -> Result.fail(ERROR_MESSAGE);
        Result<Boolean> result = SUCCESS.flatMap(flatMapFails);
        assertTrue(result.isError());
    }

    @Test
    void testFlatMapError() {
        Result<Boolean> result = FAILURE.flatMap(flatMap);
        assertTrue(result.isError());
    }

    @Test
    void testFilter() {
        Result<String> result = SUCCESS.filter(str -> true, ERROR_MESSAGE);
        assertTrue(result.isSuccess());
    }

    @Test
    void testFilterToError() {
        Result<String> result = SUCCESS.filter(str -> false, ERROR_MESSAGE);
        assertTrue(result.isError());
    }

    class ExecuteState {
        boolean executed = false;
    }

    @Test
    void testConsume() {
        ExecuteState state = new ExecuteState();
        SUCCESS.consume(str -> state.executed = true);
        assertTrue(state.executed);
    }

    @Test
    void testConsumeOnError() {
        ExecuteState state = new ExecuteState();
        FAILURE.consume(str -> state.executed = true);
        assertFalse(state.executed);
    }

    @Test
    void testConsumeOrFail() {
        ExecuteState state = new ExecuteState();
        SUCCESS.consumeOrFail(str -> state.executed = true);
        assertTrue(state.executed);
    }

    @Test
    void testConsumeOrFailOnError() {
        ExecuteState state = new ExecuteState();
        assertThrows(ResultException.class, () -> FAILURE.consumeOrFail(str -> state.executed = true));
        assertFalse(state.executed);
    }

}
