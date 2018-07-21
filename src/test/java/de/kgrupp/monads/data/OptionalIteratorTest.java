package de.kgrupp.monads.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionalIteratorTest {

    private List<String> list = Arrays.asList("A", "B");
    private OptionalIterator<String> iterator;

    @BeforeEach
     void setUp() {
        iterator = new OptionalIterator<>(list);
    }

    @Test
     void testInitialChecks() {
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasCurrent());
    }

    @Test
     void testGetCurrent() {
        assertEquals("A", iterator.getCurrent());
        assertEquals(Optional.of("A"), iterator.getSafeCurrent());
        iterator.next();
        assertEquals("B", iterator.getCurrent());
        assertEquals(Optional.of("B"), iterator.getSafeCurrent());
    }

    @Test
     void testGetIfEmtpy() {
        iterator.next();
        iterator.next();
        assertThrows(NoSuchElementException.class, iterator::getCurrent);
    }

    @Test
     void testGetSafeIfEmtpy() {
        iterator.next();
        iterator.next();
        assertFalse(iterator.getSafeCurrent().isPresent());
    }

    @Test
     void testTestOnCurrent() {
        assertTrue(iterator.testOnCurrent("A"::equals));
        assertFalse(iterator.testOnCurrent("B"::equals));
        iterator.next();
        assertTrue(iterator.testOnCurrent("B"::equals));
        iterator.next();
        assertFalse(iterator.testOnCurrent("B"::equals));
    }

    @Test
     void testNextWithMatchNotFound() {
        iterator.nextWithMatch("A"::equals);
        assertFalse(iterator.getSafeCurrent().isPresent());
    }

    @Test
    void testNextWithMatchFound() {
        iterator.nextWithMatch("B"::equals);
        assertTrue(iterator.getSafeCurrent().isPresent());
    }

}