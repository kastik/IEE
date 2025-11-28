package com.kastik.apps.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.domain.usecases.ExchangeCodeForAboardTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationScreenViewModel @Inject constructor(
    private val analytics: Analytics,
    private val exchangeCodeForToken: ExchangeCodeForAboardTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun onScreenViewed() {
        analytics.logScreenView("auth_screen")
    }

    fun onAuthRedirect(code: String?, error: String?, errorDesc: String?) {
        if (!error.isNullOrBlank()) {
            _uiState.value = UiState.Error(error)
            return
        }
        if (code.isNullOrBlank()) {
            _uiState.value = UiState.Error("Something went wrong")
            return
        }

        viewModelScope.launch {
            exchangeCodeForToken(code)
            _uiState.value = UiState.Success

        }
    }

}