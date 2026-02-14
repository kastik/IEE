package com.kastik.apps.feature.settings

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserTheme

sealed class UiState {
    data object Loading : UiState()
    data class Success(
        val theme: UserTheme,
        val sortType: SortType,
        val searchScope: SearchScope,
        val isDynamicColorEnabled: Boolean,
        val isForYouEnabled: Boolean,
        val areFabFiltersEnabled: Boolean
    ) : UiState()
}