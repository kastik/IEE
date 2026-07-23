package com.kastik.apps.feature.announcement

import androidx.annotation.StringRes
import androidx.compose.ui.text.AnnotatedString
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.model.aboard.Tag
import kotlin.time.Instant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal sealed interface AnnouncementUiState {
    data object Loading : AnnouncementUiState

    data class Success(
        val announcement: AnnouncementData,
        val shouldShowReviewDialog: Boolean,
        val isSyncing: Boolean = false,
        @StringRes val syncErrorMessageResId: Int? = null,
    ) : AnnouncementUiState

    data object Error : AnnouncementUiState
}

data class AnnouncementData(
    val title: String,
    val date: Instant,
    val author: String,
    val tags: ImmutableList<Tag> = persistentListOf(),
    val attachments: ImmutableList<Attachment> = persistentListOf(),
    val processedBodies: ImmutableList<ProcessedBody>,
)

sealed class ProcessedBody {
    data class Text(val text: AnnotatedString) : ProcessedBody()

    data class Image(val url: String) : ProcessedBody()
}
