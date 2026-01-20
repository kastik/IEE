package com.kastik.apps.feature.search

import com.kastik.apps.core.model.search.FilterOptions
import com.kastik.apps.core.model.search.QuickResults
import com.kastik.apps.core.ui.topbar.ActiveFilters

data class UiState(
    val activeFilters: ActiveFilters = ActiveFilters(),
    val availableFilters: FilterOptions = FilterOptions(),
    val quickResults: QuickResults = QuickResults(),
)