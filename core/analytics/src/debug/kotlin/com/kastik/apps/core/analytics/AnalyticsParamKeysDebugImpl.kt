package com.kastik.apps.core.analytics

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnalyticsParamKeysDebugImpl @Inject constructor() : AnalyticsParamKeys {
    override val screenName = "screen_name"
    override val screenClass = "screen_class"
    override val itemId = "item_id"
    override val itemCategory = "item_category"
    override val itemName = "item_name"
    override val searchTerm = "search_term"
    override val contentType = "content_type"
    override val preferenceValue = "preference_value"
    override val status = "status"
    override val errorMessage = "error_message"
    override val tagFilters = "tag_filters"
    override val authorFilters = "author_filters"
}
