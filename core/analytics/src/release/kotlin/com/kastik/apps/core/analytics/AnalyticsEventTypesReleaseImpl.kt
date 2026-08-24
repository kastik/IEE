package com.kastik.apps.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnalyticsEventTypesReleaseImpl @Inject constructor() : AnalyticsEventTypes {
    override val login = FirebaseAnalytics.Event.LOGIN
    override val share = FirebaseAnalytics.Event.SHARE
    override val screenView = FirebaseAnalytics.Event.SCREEN_VIEW
    override val search = FirebaseAnalytics.Event.SEARCH
    override val selectItem = FirebaseAnalytics.Event.SELECT_ITEM
    override val selectContent = FirebaseAnalytics.Event.SELECT_CONTENT
    override val viewSearchResults = FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS

    // Custom definitions
    override val logout = "logout"
    override val buttonClick = "button_click"
    override val preferencesUpdated = "preference_updated"
    override val contentLoadStatus = "content_load_status"
    override val filtersApplied = "filters_applied"
    override val bottomSheetOpened = "bottom_sheet_opened"
}
