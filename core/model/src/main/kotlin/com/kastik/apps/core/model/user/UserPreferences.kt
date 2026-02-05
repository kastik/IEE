package com.kastik.apps.core.model.user

import com.kastik.apps.core.model.aboard.SortType

data class UserPreferences(
    val theme: UserTheme,
    val dynamicColor: Boolean,
    val sortType: SortType,
    val searchScope: SearchScope,
    val enableForYou: Boolean
)
