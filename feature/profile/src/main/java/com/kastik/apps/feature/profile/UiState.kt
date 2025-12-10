package com.kastik.apps.feature.profile

import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag

sealed class UiState {
    data class Success(
        val profile: Profile,
        val subscribedTags: List<Tag>,
        val subscribableTags: List<SubscribableTag>? = null,
        val selectedSubscribableTagsIds: List<Int> = emptyList(),
        val showTagSheet: Boolean = false,
    ) : UiState()

    data class SignedOut(val message: String) : UiState()

    data object Loading : UiState()
    data class Error(val message: String) : UiState()
}