package com.kastik.apps.feature.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.common.extensions.combine
import com.kastik.apps.core.domain.usecases.GetFilterOptionsUseCase
import com.kastik.apps.core.domain.usecases.GetForYouAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetHomeAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetQuickResultsUseCase
import com.kastik.apps.core.domain.usecases.GetUserIdUseCase
import com.kastik.apps.core.domain.usecases.GetUserPreferencesUseCase
import com.kastik.apps.core.domain.usecases.IsForYouEnabledUseCase
import com.kastik.apps.core.domain.usecases.SetAnnouncementCheckTimeUseCase
import com.kastik.apps.core.domain.usecases.SetHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.ShowSignInNoticeRationaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class HomeViewModel
@Inject
constructor(
    getUserIdUseCase: GetUserIdUseCase,
    getIsSignedInUseCase: GetIsSignedInUseCase,
    showSignInNoticeRationaleUseCase: ShowSignInNoticeRationaleUseCase,
    getFilterOptionsUseCase: GetFilterOptionsUseCase,
    getForYouAnnouncementsUseCase: GetForYouAnnouncementsUseCase,
    getHomeAnnouncementsUseCase: GetHomeAnnouncementsUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
    isForYouEnabledUseCase: IsForYouEnabledUseCase,
    private val setAnnouncementCheckTimeUseCase: SetAnnouncementCheckTimeUseCase,
    private val setHasSkippedSignInUseCase: SetHasSkippedSignInUseCase,
    private val getQuickResultsUseCase: GetQuickResultsUseCase,
) : ViewModel() {

    val searchBarTextFieldState = TextFieldState()

    private val _quickSearchResultsState =
        snapshotFlow { searchBarTextFieldState.text }
            .map { it.toString() }
            .flatMapLatest { query ->
                getQuickResultsUseCase(query)
            }
    val uiState =
        combine(
                getUserIdUseCase(),
                getIsSignedInUseCase(),
                showSignInNoticeRationaleUseCase(),
                getFilterOptionsUseCase(),
                _quickSearchResultsState,
                getUserPreferencesUseCase(),
                isForYouEnabledUseCase(),
            ) {
                userId,
                isSignedIn,
                showSignInNotice,
                availableFilters,
                quickResults,
                userPreferences,
                isForYouEnabled ->
                HomeUiState(
                    userId = userId,
                    isSignedIn = isSignedIn,
                    showSignInNotice = showSignInNotice,
                    availableFilters = availableFilters,
                    quickResults = quickResults,
                    enableForYou = isForYouEnabled,
                    enableFabFilters = userPreferences.areFabFiltersEnabled,
                )
            }
            .onStart {
                viewModelScope.launch {
                    setAnnouncementCheckTimeUseCase()
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = HomeUiState(),
            )

    val homeFeedAnnouncements = getHomeAnnouncementsUseCase().cachedIn(viewModelScope)

    val forYouFeedAnnouncements = getForYouAnnouncementsUseCase().cachedIn(viewModelScope)

    fun onSignInNoticeDismiss() {
        viewModelScope.launch {
            setHasSkippedSignInUseCase(true)
        }
    }
}
