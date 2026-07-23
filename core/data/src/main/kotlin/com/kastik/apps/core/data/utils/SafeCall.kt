package com.kastik.apps.core.data.utils

import com.kastik.apps.core.model.result.Result
import kotlin.coroutines.cancellation.CancellationException

inline fun <T, E> safeCall(
    mapException: (Exception) -> E,
    recordException: (Exception) -> Unit = {},
    block: () -> T,
): Result<T, E> {
    return try {
        Result.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        recordException(e)
        Result.Error(mapException(e))
    }
}
