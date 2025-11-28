package com.kastik.apps.feature.profile

import com.kastik.apps.core.model.aboard.UserSubscribableTag
import com.kastik.apps.core.model.aboard.UserSubscribedTag

sealed class UiState {
    data class Success(
        val name: String,
        val email: String,
        val isAdmin: Boolean,
        val isAuthor: Boolean,
        val lastLogin: String,
        val createdAt: String,
        val subscribedTags: List<UserSubscribedTag>,
        val subscribableTags: List<UserSubscribableTag>? = null,
        val selectedSubscribableTagsIds: List<Int> = emptyList(),
        val showTagSheet: Boolean = false,
    ) : UiState()

    data object Loading : UiState()
    data class Error(val message: String) : UiState()
}