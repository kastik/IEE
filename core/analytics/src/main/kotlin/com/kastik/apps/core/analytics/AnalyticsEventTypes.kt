package com.kastik.apps.core.analytics

interface AnalyticsEventTypes {
    val login: String
    val logout: String
    val share: String
    val screenView: String
    val search: String
    val selectItem: String
    val selectContent: String
    val viewSearchResults: String
    val buttonClick: String
    val preferencesUpdated: String
    val contentLoadStatus: String
    val filtersApplied: String
    val bottomSheetOpened: String
}
