package com.kastik.apps.feature.profile

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.common.extensions.combine
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.GetSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.GetUserProfileUseCase
import com.kastik.apps.core.domain.usecases.SignOutUseCase
import com.kastik.apps.core.model.sync.SyncState
import com.kastik.apps.core.model.sync.isActive
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProfileViewModel
@Inject
constructor(
    getUserProfileUseCase: GetUserProfileUseCase,
    getSubscriptionsUseCase: GetSubscriptionsUseCase,
    getIsSignedInUseCase: GetIsSignedInUseCase,
    getSubscribableTagsUseCase: GetSubscribableTagsUseCase,
    private val workScheduler: WorkScheduler,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val _isSubscribeSheetVisible = MutableStateFlow(false)

    val uiState =
        combine(
                getIsSignedInUseCase(),
                getUserProfileUseCase(),
                getSubscriptionsUseCase(),
                getSubscribableTagsUseCase(),
                _isSubscribeSheetVisible,
                workScheduler.subscribeToTagsSyncState,
            ) {
                isSignedIn,
                profile,
                subscribedTags,
                subscribableTags,
                isSheetVisible,
                subscribeError ->
                if (!isSignedIn) {
                    return@combine ProfileUiState.SignedOut
                }

                if (profile == null) {
                    return@combine ProfileUiState.Loading
                }

                ProfileUiState.Success(
                    profile = profile,
                    subscribedTags = subscribedTags,
                    subscribableTags = subscribableTags,
                    isSubscribeSheetVisible = isSheetVisible,
                    isSyncingSubscriptions = subscribeError.isActive,
                    subscribeSyncErrorMessageResId = subscribeError.toSubscriptionSyncMessage(),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = ProfileUiState.Loading,
            )

    fun toggleTagsSheet(enabled: Boolean) {
        _isSubscribeSheetVisible.update {
            enabled
        }
    }

    fun updateSelectedTagIds(newSubscribedTagsIds: ImmutableList<Int>) {
        viewModelScope.launch {
            workScheduler.scheduleSubscribeToTags(newSubscribedTagsIds)
        }
    }

    fun onSignOutClick() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }
}

@StringRes
fun SyncState.toSubscriptionSyncMessage(): Int? =
    when (this) {
        SyncState.Enqueued -> R.string.sync_status_enqueued
        SyncState.Blocked -> R.string.sync_status_blocked
        SyncState.Error -> R.string.sync_status_error

        SyncState.Idle,
        SyncState.Syncing,
        SyncState.Success -> null
    }
