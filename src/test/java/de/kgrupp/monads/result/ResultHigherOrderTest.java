package de.kgrupp.monads.result;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static de.kgrupp.monads.result.AbstractResultExamples.ERROR_MESSAGE;
import static de.kgrupp.monads.result.AbstractResultExamples.FAILURE;
import static de.kgrupp.monads.result.AbstractResultExamples.INTERNAL_FAILURE;
import static de.kgrupp.monads.result.AbstractResultExamples.RESULT_OBJECT;
import static de.kgrupp.monads.result.AbstractResultExamples.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultHigherOrderTest {

    @Nested
    class Map {
        private Function<String, Boolean> map = str -> true;

        @Test
        void success() {
            Result<Boolean> result = SUCCESS.map(map);
            assertTrue(result.isSuccess());
            assertTrue(result.getObject());
        }

        @Test
        void failure() {
            Result<Boolean> result = FAILURE.map(map);
            assertTrue(result.isError());
            assertFalse(result.isInternalError());
        }

        @Test
        void internalFailure() {
            Result<Boolean> result = INTERNAL_FAILURE.map(map);
            assertTrue(result.isError());
            assertTrue(result.isInternalError());
        }
    }

    @Nested
    class CheckedMap {
        private CheckedFunction<String, Boolean> map = str -> true;

        @Test
        void success() {
            Result<Boolean> result = SUCCESS.checkedMap(map);
            assertTrue(result.isSuccess());
            assertTrue(result.getObject());
        }

        @Test
        void failure() {
            Result<Boolean> result = FAILURE.checkedMap(map);
            assertTrue(result.isError());
            assertFalse(result.isInternalError());
        }

        @Test
        void internalFailure() {
            Result<Boolean> result = INTERNAL_FAILURE.checkedMap(map);
            assertTrue(result.isError());
            assertTrue(result.isInternalError());
        }

        @Test
        void successToInternalFailure() {
            Result<Boolean> result = SUCCESS.checkedMap(obj -> {
                throw new RuntimeException("");
            });
            assertTrue(result.isError());
            assertTrue(result.isInternalError());
        }
    }

    @Nested
    class FlatMap {
        private Function<String, Result<Boolean>> flatMap = str -> Result.of(true);

        @Test
        void success() {
            Result<Boolean> result = SUCCESS.flatMap(flatMap);
            assertTrue(result.isSuccess());
            assertTrue(result.getObject());
        }

        @Test
        void successWithMap() {
            Function<String, Result<Boolean>> flatMapFails = str -> Result.fail(ERROR_MESSAGE);
            Result<Boolean> result = SUCCESS.flatMap(flatMapFails);
            assertTrue(result.isError());
        }

        @Test
        void error() {
            Result<Boolean> result = FAILURE.flatMap(flatMap);
            assertTrue(result.isError());
        }
    }

    @Nested
    class Filter {
        @Test
        void success() {
            Result<String> result = SUCCESS.filter(str -> true, ERROR_MESSAGE);
            assertTrue(result.isSuccess());
        }

        @Test
        void successToError() {
            Result<String> result = SUCCESS.filter(str -> false, ERROR_MESSAGE);
            assertTrue(result.isError());
        }
    }

    class ExecuteState {
        boolean executed = false;
    }

    @Nested
    class Consume {
        @Test
        void success() {
            ExecuteState state = new ExecuteState();
            SUCCESS.consume(str -> state.executed = true);
            assertTrue(state.executed);
        }

        @Test
        void failure() {
            ExecuteState state = new ExecuteState();
            FAILURE.consume(str -> state.executed = true);
            assertFalse(state.executed);
        }
    }

    @Nested
    class ConsumeOrThrow {
        @Test
        void success() {
            ExecuteState state = new ExecuteState();
            SUCCESS.consumeOrThrow(str -> state.executed = true);
            assertTrue(state.executed);
        }

        @Test
        void failure() {
            ExecuteState state = new ExecuteState();
            assertThrows(ResultException.class, () -> FAILURE.consumeOrThrow(str -> state.executed = true));
            assertFalse(state.executed);
        }
    }

    @Nested
    class FlatRecover {
        @Test
        void success() {
            Result<String> result = SUCCESS.flatRecover(failure -> null);
            assertEquals(SUCCESS, result);
        }

        @Test
        void error() {
            Result<String> result = FAILURE.flatRecover(failure -> SUCCESS);
            assertEquals(SUCCESS, result);
        }

        @Test
        void internalFailure() {
            Result<String> result = INTERNAL_FAILURE.flatRecover(failure -> SUCCESS);
            assertEquals(SUCCESS, result);
        }

        @Test
        void errorFails() {
            Result<String> result = FAILURE.flatRecover(failure -> INTERNAL_FAILURE);
            assertEquals(INTERNAL_FAILURE, result);
        }
    }

    @Nested
    class Recover {
        @Test
        void success() {
            Result<String> result = SUCCESS.recover(failure -> null);
            assertEquals(SUCCESS, result);
        }

        @Test
        void failure() {
            Result<String> result = FAILURE.recover(failure -> RESULT_OBJECT);
            assertEquals(SUCCESS, result);
        }
    }

    @Nested
    class FlatRunnable {
        @Test
        void success() {
            Result<String> result = SUCCESS.flatRunnable(failure -> Result.emptySuccess());
            assertEquals(SUCCESS, result);
        }

        @Test
        void failure() {
            Result<String> result = FAILURE.flatRunnable(failure -> SUCCESS);
            assertEquals(FAILURE, result);
        }
    }
}
