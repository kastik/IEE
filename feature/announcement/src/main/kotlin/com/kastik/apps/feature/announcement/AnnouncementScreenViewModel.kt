package com.kastik.apps.feature.announcement

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kastik.apps.core.domain.PrivateRefreshError
import com.kastik.apps.core.domain.Result
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.domain.usecases.DownloadAttachmentUseCase
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import com.kastik.apps.core.domain.usecases.RefreshAnnouncementWithIdUseCase
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
    private val notifier: Notifier,
    private val downloadAttachmentUseCase: DownloadAttachmentUseCase,
    private val refreshAnnouncementWithIdUseCase: RefreshAnnouncementWithIdUseCase,
    private val getAnnouncementWithIdUseCase: GetAnnouncementWithIdUseCase,
) : ViewModel() {

    val args = savedStateHandle.toRoute<AnnouncementRoute>()
    private val errorState = MutableStateFlow<String?>(null)
    val uiState: StateFlow<UiState> = combine(
        getAnnouncementWithIdUseCase(args.id),
        errorState
    ) { announcement, errorMsg ->
        if (announcement != null) {
            val parts = parseHtmlWithImages(announcement.body).toImmutableList()
            UiState.Success(announcement, parts)
        } else if (errorMsg != null) {
            UiState.Error(errorMsg)
        } else {
            UiState.Loading
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
            //TODO WIP
            when (val result = refreshAnnouncementWithIdUseCase(args.id)) {
                is Result.Success -> {
                    errorState.update { null }
                }

                is Result.Error -> {
                    when (result.error) {
                        PrivateRefreshError.Authentication -> {
                            errorState.update { "Sign in required" }
                        }

                        PrivateRefreshError.NoConnection -> {
                            notifier.sendToastNotification("No internet connection.")
                        }

                        PrivateRefreshError.Server -> {
                            notifier.sendToastNotification("There was an error while contacting the server")
                        }

                        PrivateRefreshError.Storage -> errorState.update { "Something went wrong while refreshing." }
                        PrivateRefreshError.Timeout -> errorState.update { "Timed out while refreshing." }
                        PrivateRefreshError.Unknown -> errorState.update { "Something went wrong while refreshing." }
                    }
                }
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