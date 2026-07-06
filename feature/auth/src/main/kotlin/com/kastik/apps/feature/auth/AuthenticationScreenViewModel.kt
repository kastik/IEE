package com.kastik.apps.feature.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kastik.apps.core.domain.usecases.SignInUseCase
import com.kastik.apps.core.model.result.onError
import com.kastik.apps.core.model.result.onSuccess
import com.kastik.apps.feature.auth.navigation.AuthRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class AuthenticationScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val signInUseCase: SignInUseCase,
) : ViewModel() {

    private val args = savedStateHandle.toRoute<AuthRoute>()

    val uiState: StateFlow<AuthenticationUiState> = flow {

        if (args.code.isNullOrBlank()) {
            emit(AuthenticationUiState.Error)
            return@flow
        }

        signInUseCase(args.code)
            .onSuccess {
                emit(AuthenticationUiState.Success)
                return@flow
            }.onError { error ->
                emit(AuthenticationUiState.Error)
                return@flow
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = AuthenticationUiState.Loading
    )
}