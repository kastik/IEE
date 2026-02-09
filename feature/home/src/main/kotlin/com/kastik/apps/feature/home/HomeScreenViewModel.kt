package com.kastik.apps.feature.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.domain.usecases.GetEnableForYouUseCase
import com.kastik.apps.core.domain.usecases.GetFilterOptionsUseCase
import com.kastik.apps.core.domain.usecases.GetForYouAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetHomeAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetQuickResultsUseCase
import com.kastik.apps.core.domain.usecases.RefreshAnnouncementTagsUseCase
import com.kastik.apps.core.domain.usecases.RefreshAuthorsUseCase
import com.kastik.apps.core.domain.usecases.SetUserHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.ShowSignInNoticeRationalUseCase
import com.kastik.apps.core.model.error.ConnectionError
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.error.ServerError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.error.TimeoutError
import com.kastik.apps.core.model.error.UnknownError
import com.kastik.apps.core.model.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    isSignedInUseCase: GetIsSignedInUseCase,
    showSignInNoticeRationalUseCase: ShowSignInNoticeRationalUseCase,
    getFilterOptionsUseCase: GetFilterOptionsUseCase,
    getEnableForYouUseCase: GetEnableForYouUseCase,
    getForYouAnnouncementsUseCase: GetForYouAnnouncementsUseCase,
    getHomeAnnouncementsUseCase: GetHomeAnnouncementsUseCase,
    private val notifier: Notifier,
    private val setUserHasSkippedSignInUseCase: SetUserHasSkippedSignInUseCase,
    private val refreshAuthorsUseCase: RefreshAuthorsUseCase,
    private val refreshAnnouncementTagsUseCase: RefreshAnnouncementTagsUseCase,
    private val getQuickResultsUseCase: GetQuickResultsUseCase,
) : ViewModel() {

    val searchBarTextFieldState = TextFieldState()

    private val _quickSearchResultsState = snapshotFlow { searchBarTextFieldState.text }
        .map { it.toString() }
        .flatMapLatest { query ->
            getQuickResultsUseCase(query)
        }
    val uiState = combine(
        isSignedInUseCase(),
        showSignInNoticeRationalUseCase(),
        getFilterOptionsUseCase(),
        _quickSearchResultsState,
        getEnableForYouUseCase(),
    ) { isSignedIn, showSignInNotice, availableFilters, quickResults, enableForYou ->
        UiState(
            isSignedIn = isSignedIn,
            showSignInNotice = showSignInNotice,
            availableFilters = availableFilters,
            quickResults = quickResults,
            enableForYou = enableForYou
        )
    }.onStart {
        refreshFilters()
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = UiState()
    )

    val homeFeedAnnouncements = getHomeAnnouncementsUseCase().cachedIn(viewModelScope)

    val forYouFeedAnnouncements = getForYouAnnouncementsUseCase().cachedIn(viewModelScope)

    private suspend fun refreshFilters() = coroutineScope {
        val authorsResultDeferred = async { refreshAuthorsUseCase() }
        val tagsResultDeferred = async { refreshAnnouncementTagsUseCase() }

        listOf(authorsResultDeferred.await(), tagsResultDeferred.await())
            .filterIsInstance<Result.Error<GeneralRefreshError>>()
            .firstOrNull()
            ?.let { refreshError ->
                notifier.sendToastNotification(refreshError.error.toUserMessage())
            }
    }

    fun onSignInNoticeDismiss() {
        viewModelScope.launch {
            setUserHasSkippedSignInUseCase(true)
        }
    }

}

private fun GeneralRefreshError.toUserMessage(): String = when (this) {
    ConnectionError -> "Network unavailable. Please check your connection."
    ServerError -> "Server error. Unable to refresh filters."
    StorageError -> "Storage error. Unable to cache filters."
    TimeoutError -> "Connection timed out."
    UnknownError -> "An unexpected error occurred."
}