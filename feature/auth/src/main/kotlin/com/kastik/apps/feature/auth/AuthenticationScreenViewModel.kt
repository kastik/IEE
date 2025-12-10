package com.kastik.apps.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.ExchangeCodeForAboardTokenUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.RefreshUserProfileUseCase
import com.kastik.apps.core.domain.usecases.SubscribeToTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationScreenViewModel @Inject constructor(
    private val exchangeCodeForToken: ExchangeCodeForAboardTokenUseCase,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val refreshSubscriptionsUseCase: RefreshSubscriptionsUseCase,
    private val subscribeToTagsUseCase: SubscribeToTagsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun onAuthRedirect(code: String?, error: String?, errorDesc: String?) {
        if (!error.isNullOrBlank()) {
            _uiState.value = UiState.Error(error)
            return
        }
        if (!errorDesc.isNullOrBlank()) {
            _uiState.value = UiState.Error(errorDesc)
            return
        }
        if (code.isNullOrBlank()) {
            _uiState.value = UiState.Error("Something went wrong")
            return
        }

        viewModelScope.launch {
            try {
                exchangeCodeForToken(code)
                refreshUserProfileUseCase()
                refreshSubscriptionsUseCase()
                //TODO Also re-subscribe to FCM Tags
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Something went wrong")
            }
        }
    }
}