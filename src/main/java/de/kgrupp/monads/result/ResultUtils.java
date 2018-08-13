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

    public static <T, R> Result<R> flatCombine(Iterable<Result<T>> collection, R identity, BiFunction<R, T, R> combiner) {
        return flatCombine(collection.iterator(), identity, combiner);
    }

    public static <T> Result<Stream<T>> flatCombine(Iterable<Result<T>> iterable) {
        return flatCombine(StreamSupport.stream(iterable.spliterator(), false));
    }

    public static <T, R> Result<R> flatCombine(Stream<Result<T>> stream, R identity, BiFunction<R, T, R> combiner) {
        return flatCombine(stream.iterator(), identity, combiner);
    }

    public static <T> Result<Stream<T>> flatCombine(Stream<Result<T>> stream) {
        return flatCombine(stream.map(result -> result.map(Stream::of)), Stream.empty(), Stream::concat);
    }

    private static <T, R> Result<R> flatCombine(Iterator<Result<T>> iterator, R identity, BiFunction<R, T, R> combiner) {
        if (!iterator.hasNext()) {
            return Result.of(identity);
        }
        R acc = identity;
        while (iterator.hasNext()) {
            Result<T> current = iterator.next();
            if (current.isError()) {
                return current.map(error -> null);
            }
            acc = combiner.apply(acc, current.getObject());
        }
        return Result.of(acc);
    }
}
