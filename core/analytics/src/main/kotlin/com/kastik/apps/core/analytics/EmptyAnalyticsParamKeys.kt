package com.kastik.apps.core.analytics

object EmptyAnalyticsParamKeys : AnalyticsParamKeys {
    override val SCREEN_NAME: String
        get() = ""
    override val SCREEN_CLASS: String
        get() = ""
    override val ITEM_ID: String
        get() = ""
    override val ITEM_CATEGORY: String
        get() = ""
    override val ITEM_NAME: String
        get() = ""
    override val SEARCH_TERM: String
        get() = ""
    override val CONTENT_TYPE: String
        get() = ""
    override val PREFERENCE_VALUE: String
        get() = ""
    override val STATUS: String
        get() = ""
    override val ERROR_MESSAGE: String
        get() = ""
    override val TAG_FILTERS: String
        get() = ""
    override val AUTHOR_FILTERS: String
        get() = ""
}