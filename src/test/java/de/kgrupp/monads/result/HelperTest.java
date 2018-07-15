package de.kgrupp.monads.result;

import de.kgrupp.monads.exception.UnCheckedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelperTest {

    @Test
    void testTransformSuccess() {
        // not possible for success
        assertThrows(UnCheckedException.class, () -> Helper.transform(AbstractResultExamples.SUCCESS));
    }

    @Test
    void testTransformFailure() {
        Result<Integer> result = Helper.transform(AbstractResultExamples.FAILURE);
        assertTrue(result.isError());
        assertFalse(result.isInternalError());
    }

    @Test
    void testTransformInternalFailure() {
        Result<Integer> result = Helper.transform(AbstractResultExamples.INTERNAL_FAILURE);
        assertTrue(result.isError());
        assertTrue(result.isInternalError());
    }
}
