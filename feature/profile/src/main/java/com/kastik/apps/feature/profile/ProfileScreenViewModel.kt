package com.kastik.apps.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.domain.usecases.GetUserProfileUseCase
import com.kastik.apps.core.domain.usecases.GetUserSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.GetUserSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.SubscribeToTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val analytics: Analytics,
    private val getProfileUseCase: GetUserProfileUseCase,
    private val getUserSubscriptionsUseCase: GetUserSubscriptionsUseCase,
    private val getUserSubscribableTagsUseCase: GetUserSubscribableTagsUseCase,
    private val subscribeToTagsUseCase: SubscribeToTagsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadProfile()
    }

    fun onScreenViewed() {
        analytics.logScreenView("profile_screen")
    }

    fun toggleTagsSheet() {
        val state = _uiState.value as? UiState.Success
        state?.let {
            _uiState.value = state.copy(
                showTagSheet = !state.showTagSheet
            )
        }
    }

    fun updateSelectedTagIds(newSubscribedTagsIds: List<Int>) {
        val state = _uiState.value as? UiState.Success
        state?.let {
            _uiState.value = state.copy(
                selectedSubscribableTagsIds = newSubscribedTagsIds
            )
        }
    }

    fun onApplyTags() {
        viewModelScope.launch {
            val state = _uiState.value as? UiState.Success
            state?.let {
                subscribeToTagsUseCase(state.selectedSubscribableTagsIds)
            }
            loadProfile()
        }
    }

    private fun loadProfile() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val profileDeferred = async { getProfileUseCase() }
                val subscriptionsDeferred = async { getUserSubscriptionsUseCase() }
                val subscribedTagDeferred = async { getUserSubscribableTagsUseCase() }

                val profile = profileDeferred.await()
                val subscriptions = subscriptionsDeferred.await()
                val subscribedTags = subscribedTagDeferred.await()

                _uiState.value = UiState.Success(
                    subscribedTags = subscriptions,
                    subscribableTags = subscribedTags,
                    name = profile.name,
                    email = profile.email,
                    isAdmin = profile.isAdmin,
                    isAuthor = profile.isAuthor,
                    lastLogin = profile.lastLoginAt,
                    createdAt = profile.createdAt,
                )

            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    message = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }
}
