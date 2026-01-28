package com.kastik.apps.feature.profile

import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed class UiState {
    data class Success(
        val profile: Profile,
        val subscribedTags: ImmutableList<Tag> = persistentListOf(),
        val subscribableTags: ImmutableList<SubscribableTag> = persistentListOf(),
        val showTagSheet: Boolean = false,
    ) : UiState()

    data class SignedOut(val message: String) : UiState()

    data class Loading(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}