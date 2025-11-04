package com.kastik.appsaboard.domain.repository

import com.kastik.appsaboard.domain.models.Announcement

interface AnnouncementRepository {
    suspend fun getPublicAnnouncements(): List<Announcement>
}