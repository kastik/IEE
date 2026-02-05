package com.kastik.apps.feature.settings

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserTheme

sealed class UiState {
    data object Loading : UiState()
    data class Success(
        val theme: UserTheme,
        val sortType: SortType,
        val dynamicColor: Boolean,
        val searchScope: SearchScope,
        val forYou: Boolean
    ) : UiState()
}