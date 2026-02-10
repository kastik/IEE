package com.kastik.apps.core.model.error

sealed interface GeneralRefreshError
sealed interface AuthenticatedRefreshError

data object AuthenticationError : AuthenticatedRefreshError
data object ConnectionError : GeneralRefreshError, AuthenticatedRefreshError
data object ServerError : GeneralRefreshError, AuthenticatedRefreshError
data object TimeoutError : GeneralRefreshError, AuthenticatedRefreshError
data object StorageError : GeneralRefreshError, AuthenticatedRefreshError
data object UnknownError : GeneralRefreshError, AuthenticatedRefreshError
