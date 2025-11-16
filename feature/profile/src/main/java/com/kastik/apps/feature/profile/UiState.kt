package com.kastik.apps.feature.profile

import com.kastik.apps.core.model.aboard.UserProfile
import com.kastik.apps.core.model.aboard.UserSubscribedTag

sealed class UiState {
    data class Success(
        val profile: UserProfile,
        val subscribedTag: List<UserSubscribedTag>
    ) : UiState()

    data object Loading : UiState()
    data class Error(val message: String) : UiState()
}