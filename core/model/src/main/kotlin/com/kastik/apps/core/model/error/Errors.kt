package com.kastik.apps.core.model.error

sealed interface AuthenticatedRefreshError

sealed interface GeneralRefreshError : AuthenticatedRefreshError

data object AuthenticationError : AuthenticatedRefreshError
data object ConnectionError : GeneralRefreshError
data object ServerError : GeneralRefreshError
data object TimeoutError : GeneralRefreshError
data object StorageError : GeneralRefreshError
data object UnknownError : GeneralRefreshError
