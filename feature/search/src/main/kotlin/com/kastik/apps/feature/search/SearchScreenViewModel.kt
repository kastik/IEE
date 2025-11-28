package com.kastik.apps.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.domain.usecases.GetAuthorsUseCase
import com.kastik.apps.core.domain.usecases.GetPagedFilteredAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val analytics: Analytics,
    private val getFilteredAnnouncementsUseCase: GetPagedFilteredAnnouncementsUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val getAuthorsUseCase: GetAuthorsUseCase,
) : ViewModel() {
    init {
        getAuthors()
        getTags()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun onScreenViewed() {
        analytics.logScreenView("search_screen")
    }

    fun toggleTagSheet() {
        _uiState.value = _uiState.value.copy(
            showTagSheet = !_uiState.value.showTagSheet
        )
    }

    fun toggleAuthorSheet() {
        _uiState.value = _uiState.value.copy(
            showAuthorSheet = !_uiState.value.showAuthorSheet
        )
    }


    fun getAuthors() {
        viewModelScope.launch {
            try {
                getAuthorsUseCase().collect {
                    _uiState.value = _uiState.value.copy(
                        authors = it
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    authors = null
                )
            }

        }
    }

    fun getTags() {
        viewModelScope.launch {
            try {
                getTagsUseCase().collect {
                    _uiState.value = _uiState.value.copy(
                        tags = it
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    tags = null
                )
            }

        }
    }

    fun updateQuery(newQuery: String) {
        _uiState.value = _uiState.value.copy(
            query = newQuery
        )
        updateSearchResults()
    }

    fun updateSelectedTagIds(newTags: List<Int>) {
        _uiState.value = _uiState.value.copy(
            selectedTagIds = newTags
        )
        updateSearchResults()

    }

    fun updateSelectedAuthorIdsIds(newAuthors: List<Int>) {
        _uiState.value = _uiState.value.copy(
            selectedAuthorIds = newAuthors
        )
        updateSearchResults()
    }

    private fun updateSearchResults() {
        viewModelScope.launch {
            if ((uiState.value.query.trim()
                    .isBlank()) && uiState.value.selectedAuthorIds.isEmpty() && uiState.value.selectedTagIds.isEmpty()
            ) {
                _uiState.value = _uiState.value.copy(
                    searchResults = null,
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    searchResults = getFilteredAnnouncementsUseCase(
                        uiState.value.query,
                        uiState.value.selectedAuthorIds,
                        uiState.value.selectedTagIds
                    ).cachedIn(
                        viewModelScope
                    )
                )
            }
        }
    }


}



