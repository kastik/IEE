package com.kastik.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.usecases.GetUserProfileUseCase
import com.kastik.usecases.GetUserSubscriptionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val getProfileUseCase: GetUserProfileUseCase,
    private val getUserSubscriptionsUseCase: GetUserSubscriptionsUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                // Run both requests in parallel for speed
                val profileDeferred = async { getProfileUseCase() }
                val subscriptionsDeferred = async { getUserSubscriptionsUseCase() }

                val profile = profileDeferred.await()
                val subscriptions = subscriptionsDeferred.await()

                _uiState.value = UiState.Success(
                    profile = profile,
                    subscribedTag = subscriptions   // now a List<UserSubscribedTag>
                )

            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    message = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }
}
