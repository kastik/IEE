package com.kastik.apps.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnalyticsParamKeysReleaseImpl @Inject constructor() : AnalyticsParamKeys {
    override val screenName = FirebaseAnalytics.Param.SCREEN_NAME
    override val screenClass = FirebaseAnalytics.Param.SCREEN_CLASS
    override val itemId = FirebaseAnalytics.Param.ITEM_ID
    override val itemCategory = FirebaseAnalytics.Param.ITEM_CATEGORY
    override val itemName = FirebaseAnalytics.Param.ITEM_NAME
    override val searchTerm = FirebaseAnalytics.Param.SEARCH_TERM
    override val contentType = FirebaseAnalytics.Param.CONTENT_TYPE

    // Custom definitions
    override val preferenceValue = "preference_value"
    override val status = "status"
    override val errorMessage = "error_message"
    override val tagFilters = "tag_filters"
    override val authorFilters = "author_filters"
}
