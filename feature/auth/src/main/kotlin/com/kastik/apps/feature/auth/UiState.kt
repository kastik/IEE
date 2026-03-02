package com.kastik.apps.feature.auth

import androidx.annotation.StringRes

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    data class ServerError(val message: String) : UiState()
    data class LocalError(@StringRes val resId: Int) : UiState()
}