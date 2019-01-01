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
     * @return the object which is stored in the success
     * @throws UnsupportedOperationException when called on a {@link Failure} or {@link InternalFailure}
     */
    T getObject();

    /**
     * Converts a result monad to an {@link Optional}
     *
     * @throws ResultException when result was {@link InternalFailure}
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
     * @return the reason why it failed
     * @throws UnsupportedOperationException when called on a {@link Success}
     */
    String getErrorMessage();

    /**
     * @return the exception which was thrown while the pipeline failed
     * @throws UnsupportedOperationException when called on a {@link Success} or {@link Failure}
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
     * @return the object which is stored {@link #getObject}
     * @throws ResultException when the current object is not {@link Success}
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
     * @param consumer the function which can consume the stored object
     * @throws ResultException when the current object is not {@link Success}
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