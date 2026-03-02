package com.kastik.apps.feature.profile

import androidx.annotation.StringRes
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
    data class SignedOut(@StringRes val resId: Int) : UiState()
    data class Loading(@StringRes val resId: Int) : UiState()
    data class Error(@StringRes val resId: Int) : UiState()
}