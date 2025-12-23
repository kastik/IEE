package com.kastik.apps.feature.announcement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.DownloadAttachmentUseCase
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import com.kastik.apps.core.domain.usecases.RefreshAnnouncementWithIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AnnouncementScreenViewModel.Factory::class)
class AnnouncementScreenViewModel @AssistedInject constructor(
    @Assisted val announcementId: Int,
    private val downloadAttachmentUseCase: DownloadAttachmentUseCase,
    private val refreshAnnouncementWithIdUseCase: RefreshAnnouncementWithIdUseCase,
    private val getAnnouncementWithIdUseCase: GetAnnouncementWithIdUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        getAnnouncement(announcementId)
        refreshAnnouncement(announcementId)
    }

    fun getAnnouncement(announcementId: Int) {
        viewModelScope.launch {
            getAnnouncementWithIdUseCase(announcementId)
                .catch {
                    _uiState.value = UiState.Error("Something went wrong")
                }
                .collect { announcement ->
                    _uiState.value = UiState.Success(announcement)
                }

        }
    }

    fun refreshAnnouncement(announcementId: Int) {
        viewModelScope.launch {
            runCatching {
                refreshAnnouncementWithIdUseCase(announcementId)
            }
        }
    }

    fun downloadAttachment(
        announcementId: Int,
        attachmentId: Int,
        fileName: String,
        mimeType: String
    ) {
        viewModelScope.launch {
            downloadAttachmentUseCase(
                attachmentId,
                announcementId,
                fileName = fileName,
                mimeType = mimeType
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            announcementId: Int,
        ): AnnouncementScreenViewModel
    }
}