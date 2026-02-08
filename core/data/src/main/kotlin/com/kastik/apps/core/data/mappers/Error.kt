package com.kastik.apps.core.data.mappers

import android.database.sqlite.SQLiteException
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.AuthenticationError
import com.kastik.apps.core.model.error.ConnectionError
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.error.ServerError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.error.TimeoutError
import com.kastik.apps.core.model.error.UnknownError
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toPublicRefreshError(): GeneralRefreshError {
    return when (this) {
        is SocketTimeoutException -> TimeoutError
        is IOException -> ConnectionError
        is HttpException -> ServerError
        is SQLiteException -> StorageError
        else -> UnknownError
    }
}

fun Throwable.toPrivateRefreshError(): AuthenticatedRefreshError {
    return when (this) {
        is SocketTimeoutException -> TimeoutError
        is IOException -> ConnectionError
        is HttpException -> {
            if (this.code() == 401) AuthenticationError
            else ServerError
        }

        is SQLiteException -> StorageError
        else -> UnknownError
    }
}