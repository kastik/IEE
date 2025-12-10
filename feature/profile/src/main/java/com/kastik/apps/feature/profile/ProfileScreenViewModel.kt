package com.kastik.apps.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.GetSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.GetSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.GetUserProfileUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.RefreshUserProfileUseCase
import com.kastik.apps.core.domain.usecases.SignOutUserUseCase
import com.kastik.apps.core.domain.usecases.SubscribeToTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val getProfileUseCase: GetUserProfileUseCase,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    private val refreshSubscriptionsUseCase: RefreshSubscriptionsUseCase,
    private val getSubscribableTagsUseCase: GetSubscribableTagsUseCase,
    private val refreshSubscribableTagsUseCase: RefreshSubscribableTagsUseCase,
    private val subscribeToTagsUseCase: SubscribeToTagsUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState
    private var profileCollectionJob: Job? = null

    init {
        getProfile()
        refreshData()
    }

    fun toggleTagsSheet(enabled: Boolean) {
        val state = _uiState.value as? UiState.Success
        state?.let {
            _uiState.value = state.copy(
                showTagSheet = enabled
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

    fun onSignOutClick() {
        profileCollectionJob?.cancel()
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            signOutUserUseCase()
            _uiState.value = UiState.SignedOut("You have successfully logged out")
        }
    }

    fun onApplyTags() {
        viewModelScope.launch {
            val state = _uiState.value as? UiState.Success
            state?.let {
                subscribeToTagsUseCase(state.selectedSubscribableTagsIds)
                async { refreshSubscriptionsUseCase() }.await()
            }

        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            try {
                coroutineScope {
                    async { refreshUserProfileUseCase() }.await()
                    async { refreshSubscriptionsUseCase() }.await()
                    async { refreshSubscribableTagsUseCase() }.await()
                }
            } catch (e: UnknownHostException) {
                //TODO soft error
            } catch (e: Exception) {
                signOutUserUseCase()
            }
        }
    }

    private fun getProfile() {
        profileCollectionJob = viewModelScope.launch {
            supervisorScope {
                combine(
                    getProfileUseCase(),
                    getSubscriptionsUseCase(),
                    getSubscribableTagsUseCase()
                ) { profile, subscriptions, tags ->
                    UiState.Success(
                        profile = profile,
                        subscribedTags = subscriptions,
                        subscribableTags = tags
                    )
                }.collect { successState ->
                    _uiState.value = successState
                }
            }
        }
    }
}