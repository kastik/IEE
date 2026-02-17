package com.kastik.apps.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.GetSubscribeToTagsWorkInfoUseCase
import com.kastik.apps.core.domain.usecases.GetSubscribedTagsUseCase
import com.kastik.apps.core.domain.usecases.GetUserProfileUseCase
import com.kastik.apps.core.domain.usecases.RefreshEmailSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.RefreshIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.RefreshUserProfileUseCase
import com.kastik.apps.core.domain.usecases.ScheduleSubscribeToTagsUseCase
import com.kastik.apps.core.domain.usecases.SignOutUserUseCase
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.AuthenticationError
import com.kastik.apps.core.model.error.ConnectionError
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.error.ServerError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.error.TimeoutError
import com.kastik.apps.core.model.error.UnknownError
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
    getSubscribedTagsUseCase: GetSubscribedTagsUseCase,
    getIsSignedInUseCase: GetIsSignedInUseCase,
    getSubscribableTagsUseCase: GetSubscribableTagsUseCase,
    private val notifier: Notifier,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val refreshEmailSubscriptionsUseCase: RefreshEmailSubscriptionsUseCase,
    private val refreshSubscribableTagsUseCase: RefreshSubscribableTagsUseCase,
    private val scheduleSubscribeToTagsUseCase: ScheduleSubscribeToTagsUseCase,
    private val getSubscribeToTagsWorkInfoUseCase: GetSubscribeToTagsWorkInfoUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
    private val refreshIsSignedInUseCase: RefreshIsSignedInUseCase,
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
    }.onStart { refreshData() }.catch {
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
            scheduleSubscribeToTagsUseCase(newSubscribedTagsIds)
            //TODO This won't sendToast when no connection exists
            getSubscribeToTagsWorkInfoUseCase().collect { workInfo ->
                val state = workInfo?.state
                val runCount = workInfo?.runAttemptCount ?: 0

                when {
                    state == WorkInfo.State.FAILED -> {
                        notifier.sendToastNotification("Update failed. Please try again.")
                    }

                    state == WorkInfo.State.ENQUEUED && runCount > 0 -> {
                        notifier.sendToastNotification("Connection issue. We will try again automatically.")
                    }

                    state == WorkInfo.State.ENQUEUED && runCount == 0 -> Unit
                }
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
            val refreshSubscriptionsDeferred = async { refreshEmailSubscriptionsUseCase() }
            val refreshSubscribableTagsDeferred = async { refreshSubscribableTagsUseCase() }

            listOf(
                refreshSignedInDeferred.await(),
                refreshProfileDeferred.await(),
                refreshSubscriptionsDeferred.await()
            )
                .filterIsInstance<Result.Error<AuthenticatedRefreshError>>()
                .firstOrNull()
                ?.let { refreshError ->
                    notifier.sendToastNotification(refreshError.error.toUserMessage())
                }

            val refreshSubscribableTagsResult = refreshSubscribableTagsDeferred.await()
            if (refreshSubscribableTagsResult is Result.Error<GeneralRefreshError>) {
                notifier.sendToastNotification(refreshSubscribableTagsResult.error.toUserMessage())
            }
        }
    }
}

private fun AuthenticatedRefreshError.toUserMessage(): String = when (this) {
    ConnectionError -> "Network unavailable. Please check your connection."
    ServerError -> "Server error. Unable to sync data."
    StorageError -> "Storage error. Unable to save application data."
    TimeoutError -> "Connection timed out."
    UnknownError -> "An unexpected error occurred."
    AuthenticationError -> "Sign in required"
}

private fun GeneralRefreshError.toUserMessage(): String = when (this) {
    ConnectionError -> "Network unavailable. Please check your connection."
    ServerError -> "Server error. Unable to sync data."
    StorageError -> "Storage error. Unable to save application data."
    TimeoutError -> "Connection timed out."
    UnknownError -> "An unexpected error occurred."
    AuthenticationError -> "Sign in required"
}