package com.kastik.apps.core.model.result

sealed interface Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E>(val error: E) : Result<Nothing, E>
}

inline fun <T, E> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T, E> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Error) action(error)
    return this
}

fun <T, E> Result<T, E>.getOrNull(): T? {
    return when (this) {
        is Result.Success -> data
        is Result.Error -> null
    }
}

inline fun <T, R, E> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(error)
    }
}

inline fun <T, R, E> Result<T, E>.flatMap(transform: (T) -> Result<R, E>): Result<R, E> {
    return when (this) {
        is Result.Success -> transform(data)
        is Result.Error -> Result.Error(error)
    }
}

inline fun <T, E, R> Result<T, E>.fold(
    onSuccess: (T) -> R,
    onError: (E) -> R
): R {
    return when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Error -> onError(error)
    }
}