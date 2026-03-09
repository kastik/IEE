package com.kastik.apps.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun areNotificationsEnabled(): Flow<Boolean>
    suspend fun toggleNotifications()
}