package com.kastik.apps.feature.auth

internal sealed class AuthenticationUiState {
    data object Loading : AuthenticationUiState()

    data object Error : AuthenticationUiState()

    data object Success : AuthenticationUiState()
}
