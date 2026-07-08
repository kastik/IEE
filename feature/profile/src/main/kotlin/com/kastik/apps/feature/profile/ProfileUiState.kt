package com.kastik.apps.feature.profile

import androidx.annotation.StringRes
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal sealed class ProfileUiState {
    data object Loading : ProfileUiState()

    data class Success(
        val profile: Profile,
        val subscribedTags: ImmutableList<Tag> = persistentListOf(),
        val subscribableTags: ImmutableList<Tag> = persistentListOf(),
        val isSubscribeSheetVisible: Boolean = false,
        val isSyncingSubscriptions: Boolean = false,
        @StringRes val subscribeSyncErrorMessageResId: Int? = null,
    ) : ProfileUiState()

    data object SignedOut : ProfileUiState()
}