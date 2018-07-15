package de.kgrupp.monads.result;

import java.util.Iterator;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/**
 * @author Konstantin Grupp
 */
public final class ResultUtils {

    private ResultUtils() {
        // utility class
    }

    public static <T> Result<T> combine(Iterable<Result<T>> collection, T identity, BinaryOperator<T> combine) {
        return combine(collection.iterator(), identity, combine);
    }

    public static <T> Result<T> combine(Stream<Result<T>> collection, T identity, BinaryOperator<T> combine) {
        return combine(collection.iterator(), identity, combine);
    }

    private static <T> Result<T> combine(Iterator<Result<T>> iterator, T identity, BinaryOperator<T> combine) {
        if (!iterator.hasNext()) {
            return Result.of(identity);
        }
        T acc = identity;
        while (iterator.hasNext()) {
            Result<T> current = iterator.next();
            if (current.isError()) {
                return current.map(error -> null);
            }
            acc = combine.apply(acc, current.getObject());
        }
        return Result.of(acc);
    }
}
