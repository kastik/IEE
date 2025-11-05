package com.kastik.appsaboard.domain.usecases

import com.kastik.appsaboard.domain.models.Announcement
import com.kastik.appsaboard.domain.repository.AnnouncementRepository

class GetPublicAnnouncementsUseCase(
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke(): List<Announcement> =
        announcementRepository.getPublicAnnouncements()
}

class GetAllAnnouncementsUseCase(
    private val announcementRepository: AnnouncementRepository,
) {
    suspend operator fun invoke(): List<Announcement> {
        return announcementRepository.getAllAnnouncements()
    }
}