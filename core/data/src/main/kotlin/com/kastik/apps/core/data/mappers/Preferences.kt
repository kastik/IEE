package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.datastore.proto.SearchScopeProto
import com.kastik.apps.core.datastore.proto.SortTypeProto
import com.kastik.apps.core.datastore.proto.ThemeProto
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme

fun ThemeProto.toTheme(): Theme {
    return when (this) {
        ThemeProto.System -> Theme.FOLLOW_SYSTEM
        ThemeProto.Light -> Theme.LIGHT
        ThemeProto.Dark -> Theme.DARK
        ThemeProto.UNRECOGNIZED -> Theme.FOLLOW_SYSTEM
    }
}

fun Theme.toThemeProto(): ThemeProto {
    return when (this) {
        Theme.FOLLOW_SYSTEM -> ThemeProto.System
        Theme.LIGHT -> ThemeProto.Light
        Theme.DARK -> ThemeProto.Dark
    }
}

fun SortTypeProto.toSortType(): SortType {
    return when (this) {
        SortTypeProto.ASC -> SortType.ASC
        SortTypeProto.DESC -> SortType.DESC
        SortTypeProto.Priority -> SortType.Priority
        else -> SortType.ASC
    }
}

fun SortType.toSortTypeProto(): SortTypeProto {
    return when (this) {
        SortType.ASC -> SortTypeProto.ASC
        SortType.DESC -> SortTypeProto.DESC
        SortType.Priority -> SortTypeProto.Priority
    }
}

fun SearchScopeProto.toSearchScope(): SearchScope {
    return when (this) {
        SearchScopeProto.Title -> SearchScope.Title
        SearchScopeProto.Body -> SearchScope.Body
        SearchScopeProto.TITLE_AND_BODY -> SearchScope.TitleAndBody
        SearchScopeProto.UNRECOGNIZED -> SearchScope.Title
    }
}

fun SearchScope.toSearchScopeProto(): SearchScopeProto {
    return when (this) {
        SearchScope.Title -> SearchScopeProto.Title
        SearchScope.Body -> SearchScopeProto.Body
        SearchScope.TitleAndBody -> SearchScopeProto.TITLE_AND_BODY
    }
}
