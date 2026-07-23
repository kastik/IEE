package com.kastik.apps.feature.settings

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme

internal sealed class SettingsUiState {
    data object Loading : SettingsUiState()

    data class Success(
        val theme: Theme,
        val sortType: SortType,
        val searchScope: SearchScope,
        val isDynamicColorEnabled: Boolean,
        val isForYouEnabled: Boolean,
        val isForYouAvailable: Boolean,
        val areFabFiltersEnabled: Boolean,
        val announcementCheckIntervalMinutes: Int,
        val isAnnouncementCheckIntervalAvailable: Boolean,
        val areNotificationsAllowed: Boolean,
    ) : SettingsUiState()
}
