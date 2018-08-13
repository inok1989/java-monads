package de.kgrupp.monads.result;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Konstantin Grupp
 */
public final class ResultUtils {

    private ResultUtils() {
        // utility class
    }

    public static <T, R> Result<R> combine(Iterable<Result<T>> collection, R identity, BiFunction<R, T, R> combine) {
        return combine(collection.iterator(), identity, combine);
    }

    public static <T> Result<Stream<T>> combine(Iterable<Result<T>> iterable) {
        return combine(StreamSupport.stream(iterable.spliterator(), false));
    }

    public static <T, R> Result<R> combine(Stream<Result<T>> stream, R identity, BiFunction<R, T, R> combine) {
        return combine(stream.iterator(), identity, combine);
    }

    public static <T> Result<Stream<T>> combine(Stream<Result<T>> stream) {
        return combine(stream.map(result -> result.map(Stream::of)), Stream.empty(), Stream::concat);
    }

    private static <T, R> Result<R> combine(Iterator<Result<T>> iterator, R identity, BiFunction<R, T, R> combine) {
        if (!iterator.hasNext()) {
            return Result.of(identity);
        }
        R acc = identity;
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
