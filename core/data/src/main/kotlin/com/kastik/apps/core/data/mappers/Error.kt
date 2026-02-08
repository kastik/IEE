package com.kastik.apps.core.data.mappers

import android.database.sqlite.SQLiteException
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.Authentication
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.error.NoConnection
import com.kastik.apps.core.model.error.Server
import com.kastik.apps.core.model.error.Storage
import com.kastik.apps.core.model.error.Timeout
import com.kastik.apps.core.model.error.Unknown
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toPublicRefreshError(): GeneralRefreshError {
    return when (this) {
        is SocketTimeoutException -> Timeout
        is IOException -> NoConnection
        is HttpException -> Server
        is SQLiteException -> Storage
        else -> Unknown
    }
}

fun Throwable.toPrivateRefreshError(): AuthenticatedRefreshError {
    return when (this) {
        is SocketTimeoutException -> Timeout
        is IOException -> NoConnection
        is HttpException -> {
            if (this.code() == 401) Authentication
            else Server
        }

        is SQLiteException -> Storage
        else -> Unknown
    }
}