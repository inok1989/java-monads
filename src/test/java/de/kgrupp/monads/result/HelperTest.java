package de.kgrupp.monads.result;

import de.kgrupp.monads.exception.UnCheckedException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HelperTest {

    @Test(expected = UnCheckedException.class)
    public void testTransformSuccess() {
        // not possible for success
        Helper.transform(AbstractResultTest.SUCCESS);
    }

    @Test
    public void testTransformFailure() {
        Result<Integer> result = Helper.transform(AbstractResultTest.FAILURE);
        assertTrue(result.isError());
        assertFalse(result.isInternalError());
    }

    @Test
    public void testTransformInternalFailure() {
        Result<Integer> result = Helper.transform(AbstractResultTest.INTERNAL_FAILURE);
        assertTrue(result.isError());
        assertTrue(result.isInternalError());
    }
}
