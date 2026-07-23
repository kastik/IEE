package com.kastik.apps.feature.announcement

import androidx.annotation.StringRes
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.domain.usecases.DownloadAttachmentUseCase
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import com.kastik.apps.core.domain.usecases.IncreaseImportantEventCountUseCase
import com.kastik.apps.core.domain.usecases.ResetImportantEventCountUseCase
import com.kastik.apps.core.domain.usecases.ShouldShowReviewDialogUseCase
import com.kastik.apps.core.model.sync.SyncState
import com.kastik.apps.core.model.sync.isActive
import com.kastik.apps.feature.announcement.navigation.AnnouncementRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
internal class AnnouncementViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    getAnnouncementWithIdUseCase: GetAnnouncementWithIdUseCase,
    shouldShowReviewDialogUseCase: ShouldShowReviewDialogUseCase,
    private val workScheduler: WorkScheduler,
    private val downloadAttachmentUseCase: DownloadAttachmentUseCase,
    private val increaseImportantEventCountUseCase: IncreaseImportantEventCountUseCase,
    private val resetImportantEventCountUseCase: ResetImportantEventCountUseCase,
) : ViewModel() {
    private val args = savedStateHandle.toRoute<AnnouncementRoute>()

    private val announcementDataFlow =
        getAnnouncementWithIdUseCase(args.id).map { announcement ->
            announcement?.let {
                AnnouncementData(
                    title = announcement.title,
                    date = announcement.date,
                    author = announcement.author,
                    tags = announcement.tags,
                    attachments = announcement.attachments,
                    processedBodies = it.body.parseHtmlWithImages(),
                )
            }
        }

    val uiState: StateFlow<AnnouncementUiState> =
        combine(
                announcementDataFlow,
                shouldShowReviewDialogUseCase(),
                workScheduler.announcementSyncState,
            ) { announcementData, shouldShowReviewDialog, syncState ->
                when {
                    announcementData != null ->
                        AnnouncementUiState.Success(
                            announcement = announcementData,
                            shouldShowReviewDialog = shouldShowReviewDialog,
                            isSyncing = syncState.isActive,
                            syncErrorMessageResId = syncState.toSyncMessageResId(),
                        )

                    syncState is SyncState.Error -> AnnouncementUiState.Error
                    else -> AnnouncementUiState.Loading
                }
            }
            .onStart {
                workScheduler.scheduleAnnouncementSync(args.id)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AnnouncementUiState.Loading,
            )

    fun downloadAttachment(
        attachmentId: Int,
        fileName: String,
        mimeType: String,
    ) {
        viewModelScope.launch {
            increaseImportantEventCountUseCase()
            downloadAttachmentUseCase(
                attachmentId = attachmentId,
                fileName = fileName,
                mimeType = mimeType,
            )
        }
    }

    fun onSuccessfulReview() {
        viewModelScope.launch {
            resetImportantEventCountUseCase()
        }
    }
}

@StringRes
internal fun SyncState.toSyncMessageResId(): Int? =
    when (this) {
        SyncState.Enqueued -> R.string.sync_status_enqueued
        SyncState.Blocked -> R.string.sync_status_blocked
        SyncState.Error -> R.string.sync_status_error

        SyncState.Idle,
        SyncState.Syncing,
        SyncState.Success -> null
    }

private suspend fun String.parseHtmlWithImages(): ImmutableList<ProcessedBody> {
    val imgRegex = """<img[^>]+src="([^">]+)"[^>]*>""".toRegex()
    return withContext(Dispatchers.Default) {
        val parts = mutableListOf<ProcessedBody>()
        var lastIndex = 0

        imgRegex.findAll(this@parseHtmlWithImages).forEach { match ->
            val start = match.range.first
            if (start > lastIndex) {
                val textSegment = this@parseHtmlWithImages.substring(lastIndex, start).trim()
                val parsedText = AnnotatedString.fromHtml(textSegment)
                if (parsedText.text.isNotBlank()) {
                    parts.add(ProcessedBody.Text(parsedText))
                }
            }
            val src = match.groupValues[1]
            parts.add(ProcessedBody.Image(src))
            lastIndex = match.range.last + 1
        }

        if (lastIndex < this@parseHtmlWithImages.length) {
            val remainingText = this@parseHtmlWithImages.substring(lastIndex).trim()
            val parsedText = AnnotatedString.fromHtml(remainingText)
            if (parsedText.text.isNotEmpty()) {
                parts.add(ProcessedBody.Text(parsedText))
            }
        }
        parts.toImmutableList()
    }
}
