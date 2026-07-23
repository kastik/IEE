package com.kastik.apps.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnalyticsParamKeysReleaseImpl @Inject constructor() : AnalyticsParamKeys {
    override val SCREEN_NAME = FirebaseAnalytics.Param.SCREEN_NAME
    override val SCREEN_CLASS = FirebaseAnalytics.Param.SCREEN_CLASS
    override val ITEM_ID = FirebaseAnalytics.Param.ITEM_ID
    override val ITEM_CATEGORY = FirebaseAnalytics.Param.ITEM_CATEGORY
    override val ITEM_NAME = FirebaseAnalytics.Param.ITEM_NAME
    override val SEARCH_TERM = FirebaseAnalytics.Param.SEARCH_TERM
    override val CONTENT_TYPE = FirebaseAnalytics.Param.CONTENT_TYPE

    // Custom definitions
    override val PREFERENCE_VALUE = "preference_value"
    override val STATUS = "status"
    override val ERROR_MESSAGE = "error_message"
    override val TAG_FILTERS = "tag_filters"
    override val AUTHOR_FILTERS = "author_filters"
}
