package com.kastik.announcement

import com.kastik.model.aboard.AnnouncementView

sealed class UiState() {
    data class Success(val announcement: AnnouncementView) : UiState()
    object Loading : UiState()
    object Error : UiState()

}