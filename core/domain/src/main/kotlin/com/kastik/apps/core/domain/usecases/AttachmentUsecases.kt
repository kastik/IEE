package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.service.FileDownloader
import javax.inject.Inject

class DownloadAttachmentUseCase @Inject constructor(
    private val fileDownloader: FileDownloader,
    private val announcementsRepo: AnnouncementRepository,
) {
    suspend operator fun invoke(
        attachmentId: Int,
        announcementId: Int,
        fileName: String,
        mimeType: String
    ) {
        val url = announcementsRepo.getAttachmentUrl(attachmentId)
        return fileDownloader.downloadAttachment(url, fileName, mimeType)
    }
}