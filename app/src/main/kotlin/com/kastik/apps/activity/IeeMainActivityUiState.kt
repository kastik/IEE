package com.kastik.apps.activity

import com.kastik.apps.core.model.user.Theme

sealed interface IeeMainActivityUiState {

    data object Loading : IeeMainActivityUiState

    data class Loaded(
        val theme: Theme,
        val dynamicColor: Boolean,
        val hasFinishedOnboarding: Boolean
    ) : IeeMainActivityUiState
}
