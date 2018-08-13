package de.kgrupp.monads.result;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultUtilsTest {

    private static final List<Result<String>> LIST = Arrays.asList(Result.of("A"), Result.of("B"));
    private static final List<Result<String>> LIST_WITH_ERRORS = Arrays.asList(Result.of("A"), Result.fail(AbstractResultExamples.ERROR_MESSAGE), Result.of("B"));

    @Test
    void testCombine() {
        final Result<String> result = ResultUtils.flatCombine(LIST, "", String::concat);
        assertTrue(result.isSuccess());
        assertEquals("AB", result.getObject());
    }

    @Test
    void testCombineBasic() {
        final Result<Stream<String>> result = ResultUtils.flatCombine(LIST);
        assertTrue(result.isSuccess());
        assertEquals(Optional.of("B"), result.getObject().skip(1).findFirst());
    }

    @Test
    void testCombineEmpty() {
        final Result<String> result = ResultUtils.flatCombine(Collections.emptyList(), "", String::concat);
        assertTrue(result.isSuccess());
        assertEquals("", result.getObject());
    }

    @Test
    void testCombineError() {
        Result<String> result = ResultUtils.flatCombine(LIST_WITH_ERRORS, "", String::concat);
        assertTrue(result.isError());
    }

    @Test
    void testCombineStream() {
        final Result<String> result = ResultUtils.flatCombine(LIST.stream(), "", String::concat);
        assertTrue(result.isSuccess());
        assertEquals("AB", result.getObject());
    }

    @Test
    void testCombineStreamBasic() {
        final Result<Stream<String>> result = ResultUtils.flatCombine(LIST.stream());
        assertTrue(result.isSuccess());
        assertEquals(Optional.of("B"), result.getObject().skip(1).findFirst());
    }
}
