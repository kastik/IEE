package com.kastik.profile

import com.kastik.model.aboard.UserProfile
import com.kastik.model.aboard.UserSubscribedTag

sealed class UiState {
    data class Success(
        val profile: UserProfile,
        val subscribedTag: List<UserSubscribedTag>
    ) : UiState()

    data object Loading : UiState()
    data class Error(val message: String) : UiState()
}