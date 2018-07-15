package de.kgrupp.monads.result;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResultUtilsTest {

    private static final List<Result<String>> LIST = Arrays.asList(Result.of("A"), Result.of("B"));
    private static final List<Result<String>> LIST_WITH_ERRORS = Arrays.asList(Result.of("A"), Result.fail(AbstractResultTest.ERROR_MESSAGE), Result.of("B"));

    @Test
    public void testCombine() {
        final Result<String> result = ResultUtils.combine(LIST, "", String::concat);
        assertTrue(result.isSuccess());
        assertEquals("AB", result.getObject());
    }

    @Test
    public void testCombineEmpty() {
        final Result<String> result = ResultUtils.combine(Collections.emptyList(), "", String::concat);
        assertTrue(result.isSuccess());
        assertEquals("", result.getObject());
    }

    @Test
    public void testCombineError() {
        Result<String> result = ResultUtils.combine(LIST_WITH_ERRORS, "", String::concat);
        assertTrue(result.isError());
    }

    @Test
    public void testCombineStream() {
        final Result<String> result = ResultUtils.combine(LIST.stream(), "", String::concat);
        assertTrue(result.isSuccess());
        assertEquals("AB", result.getObject());
    }
}
