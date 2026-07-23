package com.kastik.apps.feature.home

import com.kastik.apps.core.model.search.FilterOptions
import com.kastik.apps.core.model.search.QuickResults

internal data class HomeUiState(
    val userId: String? = null,
    val isSignedIn: Boolean = false,
    val showSignInNotice: Boolean = false,
    val availableFilters: FilterOptions = FilterOptions(),
    val quickResults: QuickResults = QuickResults(),
    val enableForYou: Boolean = false,
    val enableFabFilters: Boolean = false,
)
