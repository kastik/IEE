package com.kastik.apps.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.GetSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.GetUserProfileUseCase
import com.kastik.apps.core.domain.usecases.RefreshIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.RefreshUserProfileUseCase
import com.kastik.apps.core.domain.usecases.SignOutUserUseCase
import com.kastik.apps.core.domain.usecases.SubscribeToTagsUseCase
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.async
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
    getSubscriptionsUseCase: GetSubscriptionsUseCase,
    getIsSignedInUseCase: GetIsSignedInUseCase,
    getSubscribableTagsUseCase: GetSubscribableTagsUseCase,
    private val notifier: Notifier,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val refreshSubscriptionsUseCase: RefreshSubscriptionsUseCase,
    private val refreshSubscribableTagsUseCase: RefreshSubscribableTagsUseCase,
    private val subscribeToTagsUseCase: SubscribeToTagsUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
    private val refreshIsSignedInUseCase: RefreshIsSignedInUseCase,
) : ViewModel() {

    private val showTagSheet = MutableStateFlow(false)

    val uiState = combine(
        getIsSignedInUseCase(),
        getUserProfileUseCase(),
        getSubscriptionsUseCase(),
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
            UiState.SignedOut(R.string.logged_out_message)
        }
    }.onStart { refreshData() }.catch {
        UiState.Error(R.string.error_generic)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = UiState.Loading(R.string.fetching_profile_message)
    )

    fun toggleTagsSheet(enabled: Boolean) {
        showTagSheet.update {
            enabled
        }
    }

    fun updateSelectedTagIds(newSubscribedTagsIds: ImmutableList<Int>) {
        viewModelScope.launch {
            val subscribeResult = subscribeToTagsUseCase(newSubscribedTagsIds)
            val refreshResult = refreshSubscriptionsUseCase()
            if (subscribeResult is Result.Error || refreshResult is Result.Error) {
                notifier.sendToastNotification(R.string.error_generic)
            }
        }
    }

    fun onSignOutClick() {
        viewModelScope.launch {
            signOutUserUseCase()
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            val refreshSignedInDeferred = async { refreshIsSignedInUseCase() }
            val refreshProfileDeferred = async { refreshUserProfileUseCase() }
            val refreshSubscriptionsDeferred = async { refreshSubscriptionsUseCase() }
            val refreshSubscribableTagsDeferred = async { refreshSubscribableTagsUseCase() }

            listOf(
                refreshSignedInDeferred.await(),
                refreshProfileDeferred.await(),
                refreshSubscriptionsDeferred.await()
            )
                .filterIsInstance<Result.Error<NetworkError>>()
                .firstOrNull()
                ?.let { refreshError ->
                    notifier.sendToastNotification(refreshError.error.toUserMessage())
                }

            val refreshSubscribableTagsResult = refreshSubscribableTagsDeferred.await()
            if (refreshSubscribableTagsResult is Result.Error<NetworkError>) {
                notifier.sendToastNotification(refreshSubscribableTagsResult.error.toUserMessage())
            }
        }
    }
}

private fun NetworkError.toUserMessage(): Int = when (this) {
    NetworkError.Authentication -> R.string.error_authentication
    NetworkError.Connection -> R.string.error_connection
    NetworkError.ServerError -> R.string.error_server
    NetworkError.Timeout -> R.string.error_time_out
    NetworkError.Unknown -> R.string.error_generic
}