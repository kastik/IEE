package com.kastik.apps.core.analytics

import javax.inject.Inject


class AnalyticsEventTypesDebugImpl @Inject constructor(
) : AnalyticsEventTypes {
    override val LOGIN = "login"
    override val SHARE = "share"
    override val SCREEN_VIEW = "screen_view"
    override val SEARCH = "search"
    override val SELECT_ITEM = "select_item"
    override val SELECT_CONTENT = "select_content"
    override val VIEW_SEARCH_RESULTS = "view_search_results"
    override val BUTTON_CLICK = "button_click"
    override val PREFERENCE_UPDATED = "preference_updated"
    override val CONTENT_LOAD_STATUS = "content_load_status"
    override val FILTERS_APPLIED = "filters_applied"
    override val BOTTOM_SHEET_OPENED = "bottom_sheet_opened"
}