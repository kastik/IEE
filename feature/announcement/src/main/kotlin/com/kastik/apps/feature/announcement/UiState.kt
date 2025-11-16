package com.kastik.apps.feature.announcement

import com.kastik.apps.core.model.aboard.AnnouncementView

sealed class UiState() {
    data class Success(val announcement: AnnouncementView) : UiState()
    object Loading : UiState()
    object Error : UiState()

}