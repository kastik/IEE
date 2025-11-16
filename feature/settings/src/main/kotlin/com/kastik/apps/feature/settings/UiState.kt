package com.kastik.apps.feature.settings

sealed class UiState {
    data object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}