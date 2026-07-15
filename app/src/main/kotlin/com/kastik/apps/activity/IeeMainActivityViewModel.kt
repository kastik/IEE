package com.kastik.apps.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.GetHasFinishedOnboardUseCase
import com.kastik.apps.core.domain.usecases.GetUserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class IeeMainActivityViewModel @Inject constructor(
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
    getHasFinishedOnboardUseCase: GetHasFinishedOnboardUseCase,
) : ViewModel() {

    val mainActivityState: StateFlow<IeeMainActivityUiState> = combine(
        getUserPreferencesUseCase(),
        getHasFinishedOnboardUseCase(),
    ) { userPreferences, hasFinishedOnboarding ->
        IeeMainActivityUiState.Loaded(
            theme = userPreferences.theme,
            dynamicColor = userPreferences.isDynamicColorEnabled,
            hasFinishedOnboarding = hasFinishedOnboarding
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IeeMainActivityUiState.Loading
    )

}

