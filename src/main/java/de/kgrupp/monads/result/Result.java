package de.kgrupp.monads.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Konstantin Grupp
 */
public interface Result<T> {

    boolean isError();

    boolean isInternalError();

    boolean isSuccess();

    /**
     * @throws UnsupportedOperationException when called on a {@see Failure} or {@see InternalFailure}
     */
    T getObject();

    /**
     * Converts a result monad to an {@see Optional}
     *
     * @throws ResultException when result was {@see InternalFailure}
     */
    default Optional<T> asOptional() {
        if (isInternalError()) {
            throw Helper.toException(this);
        } else if (isSuccess()) {
            return Optional.ofNullable(getObject());
        } else {
            return Optional.empty();
        }
    }

    /**
     * @throws UnsupportedOperationException when called on a {@see Success}
     */
    String getErrorMessage();

    /**
     * @throws UnsupportedOperationException when called on a {@see Success} or {@see Failure}
     */
    Exception getException();

    static <T> Result<T> of(T result) {
        return new Success<>(result);
    }

    static Result<Void> emptySuccess() {
        return new Success<>(null);
    }

    // this is intended here to convert Optionals
    static <T> Result<T> of(Optional<T> result, String failureMessage) {
        return result.map(Result::of).orElse(Result.fail(failureMessage));
    }

    // this is intended here to convert Optionals
    static <T> Result<T> of(Optional<T> result, Supplier<? extends Exception> exceptionSupplier) {
        return result.map(Result::of).orElseGet(() -> Result.fail(Helper.OPTIONAL_IS_EMPTY, exceptionSupplier.get()));
    }

    static <T> Result<T> ofNullable(T result, String failureMessage) {
        return of(Optional.ofNullable(result), failureMessage);
    }

    static <T> Result<T> fail(String errorMessage) {
        return new Failure<>(errorMessage);
    }

    static <T> Result<T> fail(String errorMessage, Exception exception) {
        return new InternalFailure<>(errorMessage, exception);
    }

    default <U> Result<U> map(Function<? super T, U> mapper) {
        return flatMap(obj -> Result.of(mapper.apply(getObject())));
    }

    default <U> Result<U> checkedMap(CheckedFunction<? super T, U> mapper) {
        return flatMap(obj -> {
            try {
                return Result.of(mapper.apply(getObject()));
            } catch (Exception e) {
                return Result.fail("CheckedFunction failed with exception.", e);
            }
        });
    }

    default <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
        if (isSuccess()) {
            return mapper.apply(getObject());
        } else {
            return Helper.transform(this);
        }
    }

    default Result<T> filter(Predicate<T> predicate, String failureMessage) {
        return flatMap(result -> {
            if (predicate.test(result)) {
                return this;
            } else {
                return Result.fail(failureMessage);
            }
        });
    }

    default T orElse(T otherObject) {
        if (isSuccess()) {
            return getObject();
        } else {
            return otherObject;
        }
    }

    default T orElseGet(Supplier<? extends T> other) {
        if (isSuccess()) {
            return getObject();
        } else {
            return other.get();
        }
    }

    default T orElseThrow(Function<Failure<T>, RuntimeException> transformer) {
        if (isSuccess()) {
            return getObject();
        } else {
            throw transformer.apply((Failure<T>) this);
        }
    }

    /**
     * @throws ResultException when the current object is not {@see Success}
     */
    default T orElseThrow() {
        if (isSuccess()) {
            return getObject();
        } else {
            throw Helper.toException(this);
        }
    }

    default void consume(Consumer<T> consumer) {
        if (isSuccess()) {
            consumer.accept(getObject());
        }
    }

    /**
     * @throws ResultException when the current object is not {@see Success}
     */
    default void consumeOrThrow(Consumer<T> consumer) {
        if (isSuccess()) {
            consumer.accept(getObject());
        } else {
            throw Helper.toException(this);
        }
    }

    default Result<T> flatRecover(Function<Failure<T>, Result<T>> transformer) {
        if (isError()) {
            return transformer.apply((Failure<T>) this);
        } else {
            return this;
        }
    }

    default Result<T> recover(Function<Failure<T>, T> transformer) {
        return flatRecover(failure -> Result.of(transformer.apply(failure)));
    }

    default <O> Result<T> flatRunnable(Function<T, Result<O>> runnable) {
        if (isSuccess()) {
            return runnable.apply(getObject()).map(obj -> getObject());
        } else {
            return this;
        }
    }
}