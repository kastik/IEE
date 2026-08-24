package com.kastik.apps.core.analytics

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnalyticsEventTypesDebugImpl @Inject constructor() : AnalyticsEventTypes {
    override val login = "login"
    override val logout = "logout"
    override val share = "share"
    override val screenView = "screen_view"
    override val search = "search"
    override val selectItem = "select_item"
    override val selectContent = "select_content"
    override val viewSearchResults = "view_search_results"
    override val buttonClick = "button_click"
    override val preferencesUpdated = "preference_updated"
    override val contentLoadStatus = "content_load_status"
    override val filtersApplied = "filters_applied"
    override val bottomSheetOpened = "bottom_sheet_opened"
}
