package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.datastore.proto.Sort
import com.kastik.apps.core.datastore.proto.Theme
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.UserTheme


fun Theme.toUserTheme(): UserTheme {
    return when (this) {
        Theme.System -> UserTheme.FOLLOW_SYSTEM
        Theme.Light -> UserTheme.LIGHT
        Theme.Dark -> UserTheme.DARK
        Theme.UNRECOGNIZED -> UserTheme.FOLLOW_SYSTEM
    }
}

fun UserTheme.toTheme(): Theme {
    return when (this) {
        UserTheme.FOLLOW_SYSTEM -> Theme.System
        UserTheme.LIGHT -> Theme.Light
        UserTheme.DARK -> Theme.Dark
    }
}

fun Sort.toSortType(): SortType {
    return when (this) {
        Sort.ASC -> SortType.ASC
        Sort.DESC -> SortType.DESC
        Sort.Priority -> SortType.Priority
        else -> SortType.ASC
    }
}

fun SortType.toSort(): Sort {
    return when (this) {
        SortType.ASC -> Sort.ASC
        SortType.DESC -> Sort.DESC
        SortType.Priority -> Sort.Priority
    }
}