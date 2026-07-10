package com.kastik.apps.core.analytics

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class AnalyticsParamKeysDebugImpl @Inject constructor(
) : AnalyticsParamKeys {
    override val SCREEN_NAME = "screen_name"
    override val SCREEN_CLASS = "screen_class"
    override val ITEM_ID = "item_id"
    override val ITEM_CATEGORY = "item_category"
    override val ITEM_NAME = "item_name"
    override val SEARCH_TERM = "search_term"
    override val CONTENT_TYPE = "content_type"
    override val PREFERENCE_VALUE = "preference_value"
    override val STATUS = "status"
    override val ERROR_MESSAGE = "error_message"
    override val TAG_FILTERS = "tag_filters"
    override val AUTHOR_FILTERS = "author_filters"
}