package com.kastik.apps.feature.announcement

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.domain.usecases.DownloadAttachmentUseCase
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import com.kastik.apps.core.domain.usecases.RefreshAnnouncementWithIdUseCase
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.AuthenticationError
import com.kastik.apps.core.model.error.ConnectionError
import com.kastik.apps.core.model.error.ServerError
import com.kastik.apps.core.model.error.TimeoutError
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.feature.announcement.navigation.AnnouncementRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AnnouncementScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAnnouncementWithIdUseCase: GetAnnouncementWithIdUseCase,
    private val notifier: Notifier,
    private val downloadAttachmentUseCase: DownloadAttachmentUseCase,
    private val refreshAnnouncementWithIdUseCase: RefreshAnnouncementWithIdUseCase,
) : ViewModel() {
    val args = savedStateHandle.toRoute<AnnouncementRoute>()
    private val errorMessage = MutableStateFlow<String?>(null)
    val uiState: StateFlow<UiState> = combine(
        getAnnouncementWithIdUseCase(args.id), errorMessage
    ) { announcement, errorMsg ->
        when {
            announcement != null -> {
                val parts = parseHtmlWithImages(announcement.body).toImmutableList()
                UiState.Success(announcement, parts)
            }

            errorMsg != null -> UiState.Error(errorMsg)

            else -> UiState.Loading
        }
    }.onStart {
        refreshAnnouncement()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )

    fun refreshAnnouncement() {
        viewModelScope.launch {
            val result = refreshAnnouncementWithIdUseCase(args.id)

            if (result is Result.Success) {
                errorMessage.update { null }
                return@launch
            }

            val error = (result as Result.Error).error

            val message = error.toUserMessage()
            if (uiState.value is UiState.Success) {
                notifier.sendToastNotification(message)
            } else {
                errorMessage.update { message }
            }
        }
    }

    fun downloadAttachment(
        announcementId: Int, attachmentId: Int, fileName: String, mimeType: String
    ) {
        viewModelScope.launch {
            downloadAttachmentUseCase(
                attachmentId = attachmentId,
                announcementId = announcementId,
                fileName = fileName,
                mimeType = mimeType
            )
        }
    }

    private suspend fun parseHtmlWithImages(html: String): List<ProcessedBody> =
        withContext(Dispatchers.Default) {
            val parts = mutableListOf<ProcessedBody>()
            val regex = """<img[^>]+src="([^">]+)"[^>]*>""".toRegex()
            var lastIndex = 0

            regex.findAll(html).forEach { match ->
                val start = match.range.first
                if (start > lastIndex) {
                    val textSegment = html.substring(lastIndex, start).trim()
                    if (textSegment.isNotEmpty()) {
                        parts.add(ProcessedBody.Text(AnnotatedString.fromHtml(textSegment)))
                    }
                }
                val src = match.groupValues[1]
                parts.add(ProcessedBody.Image(src))
                lastIndex = match.range.last + 1
            }

            if (lastIndex < html.length) {
                val remainingText = html.substring(lastIndex).trim()
                if (remainingText.isNotEmpty()) {
                    parts.add(ProcessedBody.Text(AnnotatedString.fromHtml(remainingText)))
                }
            }
            parts
        }
}

private fun AuthenticatedRefreshError.toUserMessage(): String = when (this) {
    AuthenticationError -> "Sign in required"
    ConnectionError -> "No internet connection"
    ServerError -> "Couldn't refresh the announcement."
    TimeoutError -> "Timed out while refreshing."
    else -> "Something went wrong while refreshing."
}