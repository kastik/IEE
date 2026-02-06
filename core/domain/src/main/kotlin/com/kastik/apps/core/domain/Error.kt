package com.kastik.apps.core.domain

sealed interface PublicRefreshError {
    data object Timeout : PublicRefreshError
    data object NoConnection : PublicRefreshError
    data object Server : PublicRefreshError
    data object Storage : PublicRefreshError
    data object Unknown : PublicRefreshError
}

sealed interface PrivateRefreshError {
    data object Timeout : PrivateRefreshError
    data object NoConnection : PrivateRefreshError
    data object Server : PrivateRefreshError
    data object Storage : PrivateRefreshError
    data object Authentication : PrivateRefreshError
    data object Unknown : PrivateRefreshError
}

sealed interface Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E>(val error: E) : Result<Nothing, E>
}