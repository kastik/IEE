package com.kastik.apps.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.GetUserPreferencesUseCase
import com.kastik.apps.core.model.user.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class IeeMainActivityViewModel @Inject constructor(
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
) : ViewModel() {

    val appState: StateFlow<IeeAppState> = getUserPreferencesUseCase().map { userPreferences ->
        IeeAppState(
            userPreferences.theme,
            userPreferences.isDynamicColorEnabled
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IeeAppState(Theme.FOLLOW_SYSTEM, true)
    )

}

