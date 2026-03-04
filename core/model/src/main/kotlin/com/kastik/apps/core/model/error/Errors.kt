package com.kastik.apps.core.model.error

interface AppError

sealed class NetworkError : AppError {
    data object Connection : NetworkError()
    data object Timeout : NetworkError()
    data object Authentication : NetworkError()
    data object ServerError : NetworkError()
    data object Unknown : NetworkError()
}

sealed class LocalError : AppError {
    data object DiskFull : LocalError()
    data object DatabaseCorrupt : LocalError()
    data object DataStoreCorrupt : LocalError()
    data object ConstraintViolation : LocalError()
    data object IOError : LocalError()
    data object Unknown : LocalError()
}