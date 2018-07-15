package de.kgrupp.monads.data;

import de.kgrupp.monads.exception.UnCheckedException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OptionalIteratorTest {

    private List<String> list = Arrays.asList("A", "B");
    private OptionalIterator<String> iterator;

    @Before
    public void setUp() {
        iterator = new OptionalIterator<>(list);
    }

    @Test
    public void testInitialChecks() {
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasCurrent());
    }

    @Test
    public void testGetCurrent() {
        assertEquals("A", iterator.getCurrent());
        assertEquals(Optional.of("A"), iterator.getSafeCurrent());
        iterator.next();
        assertEquals("B", iterator.getCurrent());
        assertEquals(Optional.of("B"), iterator.getSafeCurrent());
    }

    @Test(expected = UnCheckedException.class)
    public void testGetIfEmtpy() {
        iterator.next();
        iterator.next();
        iterator.getCurrent();
    }

    @Test
    public void testGetSafeIfEmtpy() {
        iterator.next();
        iterator.next();
        assertFalse(iterator.getSafeCurrent().isPresent());
    }

    @Test
    public void testTestOnCurrent() {
        assertTrue(iterator.testOnCurrent("A"::equals));
        assertFalse(iterator.testOnCurrent("B"::equals));
        iterator.next();
        assertTrue(iterator.testOnCurrent("B"::equals));
        iterator.next();
        assertFalse(iterator.testOnCurrent("B"::equals));
    }

    @Test
    public void testNextWithMatchNotFound() {
        iterator.nextWithMatch("A"::equals);
        assertFalse(iterator.getSafeCurrent().isPresent());
    }

    @Test
    public void testNextWithMatchFound() {
        iterator.nextWithMatch("B"::equals);
        assertTrue(iterator.getSafeCurrent().isPresent());
    }

}