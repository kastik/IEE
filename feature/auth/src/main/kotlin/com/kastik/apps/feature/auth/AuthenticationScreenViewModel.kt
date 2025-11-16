package com.kastik.apps.feature.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.ExchangeCodeForAboardTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationScreenViewModel @Inject constructor(
    private val exchangeCodeForToken: ExchangeCodeForAboardTokenUseCase
) : ViewModel() {

    private val _uiState: MutableState<UiState> = mutableStateOf(UiState.Loading)
    val uiState: State<UiState> = _uiState

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