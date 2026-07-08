package com.kastik.apps.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AnalyticsEventTypesReleaseImpl @Inject constructor(
) : AnalyticsEventTypes {
    override val LOGIN = FirebaseAnalytics.Event.LOGIN
    override val SHARE = FirebaseAnalytics.Event.SHARE
    override val SCREEN_VIEW = FirebaseAnalytics.Event.SCREEN_VIEW
    override val SEARCH = FirebaseAnalytics.Event.SEARCH
    override val SELECT_ITEM = FirebaseAnalytics.Event.SELECT_ITEM
    override val SELECT_CONTENT = FirebaseAnalytics.Event.SELECT_CONTENT
    override val VIEW_SEARCH_RESULTS = FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS

    //Custom definitions
    override val LOGOUT = "logout"
    override val BUTTON_CLICK = "button_click"
    override val PREFERENCE_UPDATED = "preference_updated"
    override val CONTENT_LOAD_STATUS = "content_load_status"
    override val FILTERS_APPLIED = "filters_applied"
    override val BOTTOM_SHEET_OPENED = "bottom_sheet_opened"
}