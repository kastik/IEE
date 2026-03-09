package com.kastik.apps.core.data.mappers

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteFullException
import androidx.datastore.core.CorruptionException
import com.kastik.apps.core.model.error.LocalError
import com.kastik.apps.core.model.error.NetworkError
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun Throwable.toLocalError(): LocalError {
    return when (this) {
        is SQLiteFullException -> LocalError.DiskFull
        is SQLiteDatabaseCorruptException -> LocalError.DatabaseCorrupt
        is SQLiteConstraintException -> LocalError.ConstraintViolation

        is CorruptionException -> LocalError.DataStoreCorrupt

        is SQLiteDiskIOException, is IOException -> LocalError.IOError

        else -> LocalError.Unknown
    }
}


fun Throwable.toNetworkError(): NetworkError {
    return when (this) {
        is UnknownHostException, is ConnectException -> NetworkError.Connection
        is SocketTimeoutException -> NetworkError.Timeout
        is IOException -> NetworkError.Connection

        is HttpException -> {
            when (this.code()) {
                401 -> NetworkError.Authentication
                in 500..599 -> NetworkError.ServerError
                else -> NetworkError.Unknown
            }
        }

        else -> NetworkError.Unknown
    }
}