package com.kastik.apps.core.data.mappers

import android.database.sqlite.SQLiteException
import com.kastik.apps.core.domain.PrivateRefreshError
import com.kastik.apps.core.domain.PublicRefreshError
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toPublicRefreshError(): PublicRefreshError {
    return when (this) {
        is SocketTimeoutException -> PublicRefreshError.Timeout
        is IOException -> PublicRefreshError.NoConnection
        is HttpException -> PublicRefreshError.Server
        is SQLiteException -> PublicRefreshError.Storage
        else -> PublicRefreshError.Unknown
    }
}

fun Throwable.toPrivateRefreshError(): PrivateRefreshError {
    return when (this) {
        is SocketTimeoutException -> PrivateRefreshError.Timeout
        is IOException -> PrivateRefreshError.NoConnection
        is HttpException -> {
            if (this.code() == 401) PrivateRefreshError.Authentication
            else PrivateRefreshError.Server
        }

        is SQLiteException -> PrivateRefreshError.Storage
        else -> PrivateRefreshError.Unknown
    }
}