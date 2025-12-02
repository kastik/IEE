package com.kastik.apps.feature.announcement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnnouncementScreenViewModel @Inject constructor(
    private val getAnnouncementWithIdUseCase: GetAnnouncementWithIdUseCase,
    private val analytics: Analytics
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun getAnnouncement(announcementId: Int) {
        viewModelScope.launch {
            try {
                getAnnouncementWithIdUseCase(announcementId).collect { announcement ->
                    _uiState.value = UiState.Success(announcement)
                    }
            } catch (_: Exception) {
                _uiState.value = UiState.Error
            }
        }
    }
}