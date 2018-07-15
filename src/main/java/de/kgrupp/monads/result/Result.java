package de.kgrupp.monads.result;

import de.kgrupp.monads.exception.UnCheckedException;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Konstantin Grupp
 */
public interface Result<T> {

    String SUCCESS = "SUCCESS";
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

    String getSuccessMessage();

    String getErrorMessage();

    Throwable getThrowable();

    static <T> Result<T> of(T result) {
        return new Success<>(SUCCESS, result);
    }

    static Result<Void> emptySuccess(String successMessage) {
        return new Success<>(successMessage, null);
    }

    static <T> Result<T> of(String successMessage, T result) {
        return new Success<>(successMessage, result);
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
        if (isSuccess()) {
            return Result.of(mapper.apply(getObject()));
        } else {
            return Helper.transform(this);
        }
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

    default T orElseThrow() {
        if (isSuccess()) {
            return getObject();
        } else {
            throw new UnCheckedException(getErrorMessage());
        }
    }
}