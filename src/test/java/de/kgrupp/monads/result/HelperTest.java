package de.kgrupp.monads.result;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelperTest {

    @Nested
    class Transform {
        @Test
        void success() {
            // not possible for success
            assertThrows(IllegalArgumentException.class, () -> Helper.transform(AbstractResultExamples.SUCCESS));
        }

        @Test
        void failure() {
            Result<Integer> result = Helper.transform(AbstractResultExamples.FAILURE);
            assertTrue(result.isError());
            assertFalse(result.isInternalError());
        }

        @Test
        void internalFailure() {
            Result<Integer> result = Helper.transform(AbstractResultExamples.INTERNAL_FAILURE);
            assertTrue(result.isError());
            assertTrue(result.isInternalError());
        }
    }

    @Nested
    class ToException {
        @Test
        void success() {
            // not possible for success
            assertThrows(IllegalArgumentException.class, () -> Helper.toException(AbstractResultExamples.SUCCESS));
        }
    }
}
