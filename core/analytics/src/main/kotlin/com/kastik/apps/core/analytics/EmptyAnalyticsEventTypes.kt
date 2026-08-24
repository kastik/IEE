package com.kastik.apps.core.analytics

object EmptyAnalyticsEventTypes : AnalyticsEventTypes {
    override val login: String
        get() = ""

    override val logout: String
        get() = ""

    override val share: String
        get() = ""

    override val screenView: String
        get() = ""

    override val search: String
        get() = ""

    override val selectItem: String
        get() = ""

    override val selectContent: String
        get() = ""

    override val viewSearchResults: String
        get() = ""

    override val buttonClick: String
        get() = ""

    override val preferencesUpdated: String
        get() = ""

    override val contentLoadStatus: String
        get() = ""

    override val filtersApplied: String
        get() = ""

    override val bottomSheetOpened: String
        get() = ""
}
