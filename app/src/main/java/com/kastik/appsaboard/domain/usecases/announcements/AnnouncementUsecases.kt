package com.kastik.appsaboard.domain.usecases.announcements

import com.kastik.appsaboard.domain.models.Announcement
import com.kastik.appsaboard.domain.repository.AnnouncementRepository

class GetPublicAnnouncementsUseCase(
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke(): List<Announcement> =
        announcementRepository.getPublicAnnouncements()
}
