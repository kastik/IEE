package com.kastik.apps.core.analytics

object EmptyAnalyticsEventTypes : AnalyticsEventTypes {
    override val LOGIN: String
        get() = ""

    override val LOGOUT: String
        get() = ""

    override val SHARE: String
        get() = ""

    override val SCREEN_VIEW: String
        get() = ""

    override val SEARCH: String
        get() = ""

    override val SELECT_ITEM: String
        get() = ""

    override val SELECT_CONTENT: String
        get() = ""

    override val VIEW_SEARCH_RESULTS: String
        get() = ""

    override val BUTTON_CLICK: String
        get() = ""

    override val PREFERENCE_UPDATED: String
        get() = ""

    override val CONTENT_LOAD_STATUS: String
        get() = ""

    override val FILTERS_APPLIED: String
        get() = ""

    override val BOTTOM_SHEET_OPENED: String
        get() = ""
}
