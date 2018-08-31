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

    String INTERNAL_FAILURE = "INTERNAL_FAILURE";

    boolean isError();

    boolean isInternalError();

    boolean isSuccess();

    T getObject();

    default Optional<T> asOptional() {
        if (isSuccess()) {
            return Optional.ofNullable(getObject());
        } else {
            return Optional.empty();
        }
    }

    String getErrorMessage();

    Throwable getThrowable();

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
    static <T> Result<T> of(Optional<T> result, Supplier<? extends Throwable> throwableSupplier) {
        return result.map(Result::of).orElseGet(() -> Result.fail(throwableSupplier.get()));
    }

    static <T> Result<T> ofNullable(T result, String failureMessage) {
        return of(Optional.ofNullable(result), failureMessage);
    }

    static <T> Result<T> fail(String errorMessage) {
        return new Failure<>(errorMessage);
    }

    static <T> Result<T> fail(Throwable throwable) {
        return new InternalFailure<>(INTERNAL_FAILURE, throwable);
    }

    default <U> Result<U> map(Function<? super T, U> mapper) {
        return flatMap(obj -> Result.of(mapper.apply(getObject())));
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

    default void consumeOrFail(Consumer<T> consumer) {
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
}