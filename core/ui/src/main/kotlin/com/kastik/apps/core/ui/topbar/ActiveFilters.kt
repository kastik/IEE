package com.kastik.apps.core.ui.topbar

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ActiveFilters(
    val committedQuery: String = "",
    val selectedTagIds: ImmutableList<Int> = persistentListOf(),
    val selectedAuthorIds: ImmutableList<Int> = persistentListOf(),
)
