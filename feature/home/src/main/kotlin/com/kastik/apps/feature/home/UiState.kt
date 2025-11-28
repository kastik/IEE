package com.kastik.apps.feature.home

data class UiState(
    val isSignedIn: Boolean = false,
    val showSignInNotice: Boolean = false,
    val hasEvaluatedAuth: Boolean = false,
)

sealed interface HomeEvent {
    data class OpenUrl(val url: String) : HomeEvent
}