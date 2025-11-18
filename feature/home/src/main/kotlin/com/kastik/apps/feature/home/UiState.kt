package com.kastik.apps.feature.home

import androidx.paging.PagingData
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import kotlinx.coroutines.flow.Flow

data class UiState(
    val isSignedIn: Boolean = false,
    val showSignInNotice: Boolean = false,
    val hasEvaluatedAuth: Boolean = false,
    val announcements: Flow<PagingData<AnnouncementPreview>>,
)

sealed interface HomeEvent {
    data class OpenUrl(val url: String) : HomeEvent
}