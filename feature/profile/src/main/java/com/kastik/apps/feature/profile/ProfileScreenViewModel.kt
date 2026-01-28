package com.kastik.apps.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.GetSubscribedTagsUseCase
import com.kastik.apps.core.domain.usecases.GetUserProfileUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.RefreshUserProfileUseCase
import com.kastik.apps.core.domain.usecases.SignOutUserUseCase
import com.kastik.apps.core.domain.usecases.SubscribeToTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    getUserProfileUseCase: GetUserProfileUseCase,
    getSubscribedTagsUseCase: GetSubscribedTagsUseCase,
    getIsSignedInUseCase: GetIsSignedInUseCase,
    getSubscribableTagsUseCase: GetSubscribableTagsUseCase,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val refreshSubscriptionsUseCase: RefreshSubscriptionsUseCase,
    private val refreshSubscribableTagsUseCase: RefreshSubscribableTagsUseCase,
    private val subscribeToTagsUseCase: SubscribeToTagsUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
) : ViewModel() {

    private val showTagSheet = MutableStateFlow(false)

    val uiState = combine(
        getIsSignedInUseCase(),
        getUserProfileUseCase(),
        getSubscribedTagsUseCase(),
        getSubscribableTagsUseCase(),
        showTagSheet,
    ) { isSignedIn, profile, subscribedTags, subscribableTags, showTagSheet ->
        if (isSignedIn) {
            UiState.Success(
                profile = profile,
                subscribedTags = subscribedTags,
                subscribableTags = subscribableTags,
                showTagSheet = showTagSheet,
            )
        } else {
            UiState.SignedOut("You have beed logged out.")
        }
    }.onStart {
        runCatching { refreshData() }
    }.catch {
        UiState.Error(it.message ?: "Something went wrong")
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = UiState.Loading(message = "Fetching your profile...")
    )

    fun toggleTagsSheet(enabled: Boolean) {
        showTagSheet.update {
            enabled
        }
    }

    fun updateSelectedTagIds(newSubscribedTagsIds: ImmutableList<Int>) {
        viewModelScope.launch {
            runCatching { subscribeToTagsUseCase(newSubscribedTagsIds) }
            runCatching { refreshSubscriptionsUseCase() }
        }
    }

    fun onSignOutClick() {
        viewModelScope.launch {
            signOutUserUseCase()
        }
    }

    private suspend fun refreshData() {
        coroutineScope {
            launch { refreshUserProfileUseCase() }
            launch { refreshSubscriptionsUseCase() }
            launch { refreshSubscribableTagsUseCase() }
        }
    }
}