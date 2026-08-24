package com.kastik.apps.feature.auth

internal sealed interface AuthenticationUiState {
    data object Loading : AuthenticationUiState

    data object Error : AuthenticationUiState

    data object Success : AuthenticationUiState
}
