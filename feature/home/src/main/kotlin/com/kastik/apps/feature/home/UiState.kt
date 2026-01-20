package com.kastik.apps.feature.home

import com.kastik.apps.core.model.search.FilterOptions
import com.kastik.apps.core.model.search.QuickResults

data class UiState(
    val isSignedIn: Boolean = false,
    val showSignInNotice: Boolean = false,
    val availableFilters: FilterOptions = FilterOptions(),
    val quickResults: QuickResults = QuickResults(),
)