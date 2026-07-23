package com.kastik.apps.core.model.user

import com.kastik.apps.core.model.aboard.SortType
import kotlin.time.Instant

data class UserPreferences(
    val theme: Theme,
    val isDynamicColorEnabled: Boolean,
    val sortType: SortType,
    val searchScope: SearchScope,
    val isForYouEnabled: Boolean,
    val areFabFiltersEnabled: Boolean,
    val checkIntervalMinutes: Int,
    val hasSkippedSignedIn: Boolean,
    val importantEventCount: Int,
    val lastNotificationCheckTime: Instant?,
)
