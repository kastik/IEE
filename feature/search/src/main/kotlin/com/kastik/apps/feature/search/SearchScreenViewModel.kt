package com.kastik.apps.feature.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.domain.usecases.GetFilterOptionsUseCase
import com.kastik.apps.core.domain.usecases.GetFilteredAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetQuickResultsUseCase
import com.kastik.apps.core.domain.usecases.RefreshAuthorsUseCase
import com.kastik.apps.core.model.error.ConnectionError
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.error.ServerError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.error.TimeoutError
import com.kastik.apps.core.model.error.UnknownError
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.ui.topbar.ActiveFilters
import com.kastik.apps.feature.search.navigation.SearchRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getFilterOptionsUseCase: GetFilterOptionsUseCase,
    private val notifier: Notifier,
    private val refreshAuthorsUseCase: RefreshAuthorsUseCase,
    private val refreshAnnouncementTagsUseCase: RefreshAuthorsUseCase,
    private val getQuickResultsUseCase: GetQuickResultsUseCase,
    private val getFilteredAnnouncementsUseCase: GetFilteredAnnouncementsUseCase,
) : ViewModel() {

    private val args = savedStateHandle.toRoute<SearchRoute>()
    val searchBarTextFieldState = TextFieldState(
        initialText = args.query
    )

    private val _availableFilters = getFilterOptionsUseCase().onStart {
        refreshFilters()
    }

    private val _quickSearchResultsState =
        snapshotFlow { searchBarTextFieldState.text }.map { it.toString() }.flatMapLatest { query ->
            getQuickResultsUseCase(query)
        }

    private val _activeFeedFilters = MutableStateFlow(
        ActiveFilters(
            committedQuery = args.query,
            selectedTagIds = args.tags.toImmutableList(),
            selectedAuthorIds = args.authors.toImmutableList()
        )
    )

    val searchFeedAnnouncements = _activeFeedFilters.flatMapLatest { activeFilters ->
        getFilteredAnnouncementsUseCase(
            activeFilters.committedQuery,
            activeFilters.selectedAuthorIds,
            activeFilters.selectedTagIds
        ).cachedIn(
            viewModelScope
        )
    }

    val uiState = combine(
        _availableFilters, _activeFeedFilters, _quickSearchResultsState
    ) { availableFilters, activeFilters, quickResults ->
        UiState(
            availableFilters = availableFilters,
            activeFilters = activeFilters,
            quickResults = quickResults
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = UiState()
    )

    private suspend fun refreshFilters() = coroutineScope {
        val authorsResultDeferred = async { refreshAuthorsUseCase() }
        val tagsResultDeferred = async { refreshAnnouncementTagsUseCase() }

        listOf(
            authorsResultDeferred.await(),
            tagsResultDeferred.await()
        ).filterIsInstance<Result.Error<GeneralRefreshError>>().firstOrNull()?.let { refreshError ->
            notifier.sendToastNotification(refreshError.error.toUserMessage())
        }
    }

    fun onSearch(
        query: String, tagIds: ImmutableList<Int>, authorIds: ImmutableList<Int>
    ) {
        searchBarTextFieldState.edit {
            delete(0, length)
            append(query)
        }
        _activeFeedFilters.update { filters ->
            filters.copy(
                committedQuery = query,
                selectedTagIds = tagIds.distinct().toImmutableList(),
                selectedAuthorIds = authorIds.distinct().toImmutableList()
            )
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