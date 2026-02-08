package com.kastik.apps.core.model.error

sealed interface GeneralRefreshError
sealed interface AuthenticatedRefreshError

data object NoConnection : GeneralRefreshError, AuthenticatedRefreshError
data object Server : GeneralRefreshError, AuthenticatedRefreshError
data object Timeout : GeneralRefreshError, AuthenticatedRefreshError
data object Storage : GeneralRefreshError, AuthenticatedRefreshError
data object Unknown : GeneralRefreshError, AuthenticatedRefreshError
data object Authentication : AuthenticatedRefreshError