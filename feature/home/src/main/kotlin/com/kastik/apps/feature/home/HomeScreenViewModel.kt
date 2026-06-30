package com.kastik.apps.feature.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.domain.usecases.GetFilterOptionsUseCase
import com.kastik.apps.core.domain.usecases.GetForYouAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetHomeAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetQuickResultsUseCase
import com.kastik.apps.core.domain.usecases.GetUserPreferencesUseCase
import com.kastik.apps.core.domain.usecases.RefreshFilterOptionsUseCase
import com.kastik.apps.core.domain.usecases.SetAnnouncementCheckTimeUseCase
import com.kastik.apps.core.domain.usecases.SetHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.ShowSignInNoticeRationalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    getForYouAnnouncementsUseCase: GetForYouAnnouncementsUseCase,
    getHomeAnnouncementsUseCase: GetHomeAnnouncementsUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val setAnnouncementCheckTimeUseCase: SetAnnouncementCheckTimeUseCase,
    private val refreshFilterOptionsUseCase: RefreshFilterOptionsUseCase,
    private val setHasSkippedSignInUseCase: SetHasSkippedSignInUseCase,
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
        getUserPreferencesUseCase(),
    ) { isSignedIn, showSignInNotice, availableFilters, quickResults, userPreferences ->
        UiState(
            isSignedIn = isSignedIn,
            showSignInNotice = showSignInNotice,
            availableFilters = availableFilters,
            quickResults = quickResults,
            enableForYou = userPreferences.isForYouEnabled,
            enableFabFilters = userPreferences.areFabFiltersEnabled
        )
    }.onStart {
        viewModelScope.launch {
            setAnnouncementCheckTimeUseCase()
        }
        viewModelScope.launch {
            refreshFilterOptionsUseCase()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = UiState()
    )

    val homeFeedAnnouncements = getHomeAnnouncementsUseCase().cachedIn(viewModelScope)

    val forYouFeedAnnouncements = getForYouAnnouncementsUseCase().cachedIn(viewModelScope)

    fun onSignInNoticeDismiss() {
        viewModelScope.launch {
            setHasSkippedSignInUseCase(true)
        }
    }

}