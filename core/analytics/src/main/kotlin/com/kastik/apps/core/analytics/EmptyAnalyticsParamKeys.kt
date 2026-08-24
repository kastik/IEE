package com.kastik.apps.core.analytics

object EmptyAnalyticsParamKeys : AnalyticsParamKeys {
    override val screenName: String
        get() = ""

    override val screenClass: String
        get() = ""

    override val itemId: String
        get() = ""

    override val itemCategory: String
        get() = ""

    override val itemName: String
        get() = ""

    override val searchTerm: String
        get() = ""

    override val contentType: String
        get() = ""

    override val preferenceValue: String
        get() = ""

    override val status: String
        get() = ""

    override val errorMessage: String
        get() = ""

    override val tagFilters: String
        get() = ""

    override val authorFilters: String
        get() = ""
}
