package de.kgrupp.monads.data;

import de.kgrupp.monads.exception.UnCheckedException;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Konstantin Grupp
 */
public class OptionalIterator<E> {

    private Iterator<E> iterator;
    private E current;

    public OptionalIterator(Iterable<E> iterable) {
        this.iterator = iterable.iterator();
        next();
    }

    public boolean hasCurrent() {
        return current != null;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public E getCurrent() {
        if (current == null) {
            throw new UnCheckedException("Not possible to get current");
        }
        return current;
    }

    public Optional<E> getSafeCurrent() {
        return Optional.ofNullable(current);
    }

    public void next() {
        if (iterator.hasNext()) {
            this.current = iterator.next();
        } else {
            this.current = null;
        }
    }

    public boolean testOnCurrent(Predicate<E> predicate) {
        if (hasCurrent()) {
            return predicate.test(current);
        } else {
            return false;
        }
    }

    public void nextWithMatch(Predicate<E> predicate) {
        next();
        while (!testOnCurrent(predicate)) {
            next();
            if (!hasCurrent()) {
                break;
            }
        }
    }
}
