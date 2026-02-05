package com.kastik.apps.feature.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.domain.usecases.GetEnableForYouUseCase
import com.kastik.apps.core.domain.usecases.GetFilterOptionsUseCase
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetPagedAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetPagedFilteredAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetQuickResultsUseCase
import com.kastik.apps.core.domain.usecases.GetSubscribedTagsUseCase
import com.kastik.apps.core.domain.usecases.RefreshFilterOptionsUseCase
import com.kastik.apps.core.domain.usecases.RefreshIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.SetUserHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.ShowSignInNoticeRationalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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
    getSubscribedTagsUseCase: GetSubscribedTagsUseCase,
    private val getPagedFilteredAnnouncementsUseCase: GetPagedFilteredAnnouncementsUseCase,
    private val getPagedAnnouncements: GetPagedAnnouncementsUseCase,
    private val setUserHasSkippedSignInUseCase: SetUserHasSkippedSignInUseCase,
    private val refreshFilterOptionsUseCase: RefreshFilterOptionsUseCase,
    private val getQuickResultsUseCase: GetQuickResultsUseCase,
    private val refreshIsSignedInUseCase: RefreshIsSignedInUseCase
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
        viewModelScope.launch {
            runCatching { refreshFilterOptionsUseCase() }
            runCatching { refreshIsSignedInUseCase }
        }
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = UiState()
    )

    val homeFeedAnnouncements = uiState.map { it.isSignedIn }.distinctUntilChanged()
        .flatMapLatest { _ -> getPagedAnnouncements() }.cachedIn(viewModelScope)

    val subscribedTags = getSubscribedTagsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            emptyList()
        )

    val forYouFeedAnnouncements = subscribedTags.flatMapLatest { tags ->
        getPagedFilteredAnnouncementsUseCase(
            tagIds = tags.map { it.id }
        )
    }.cachedIn(viewModelScope)


    fun onSignInNoticeDismiss() {
        viewModelScope.launch {
            setUserHasSkippedSignInUseCase(true)
        }
    }

}