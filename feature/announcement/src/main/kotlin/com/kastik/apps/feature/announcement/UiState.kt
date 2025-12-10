package com.kastik.apps.feature.announcement

import com.kastik.apps.core.model.aboard.AnnouncementView

sealed class UiState() {
    data object Loading : UiState()
    data class Success(val announcement: AnnouncementView) : UiState()
    data class Error(val message: String) : UiState()

}