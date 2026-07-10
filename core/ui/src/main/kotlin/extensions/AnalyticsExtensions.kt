package com.kastik.apps.core.ui.extensions

import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.analytics.AnalyticsEvent
import com.kastik.apps.core.analytics.AnalyticsEvent.Param


fun Analytics.logAnnouncementShared(announcementId: Int) = logEvent(
    AnalyticsEvent(
        type = types.SHARE,
        extras = listOf(
            Param(paramKeys.ITEM_ID, announcementId.toString()),
            Param(paramKeys.CONTENT_TYPE, "announcement"),
        ),
    ),
)

private fun Analytics.logPreferenceChanged(preferenceName: String, value: String) = logEvent(
    AnalyticsEvent(
        type = types.PREFERENCE_UPDATED,
        extras = listOf(
            Param(paramKeys.ITEM_ID, preferenceName),
            Param(paramKeys.PREFERENCE_VALUE, value)
        ),
    ),
)

fun Analytics.logSortTypePreferenceChanged(sortTypeName: String) =
    logPreferenceChanged("sort_type", sortTypeName)

fun Analytics.logSearchScopePreferenceChanged(searchScopeName: String) =
    logPreferenceChanged("search_scope", searchScopeName)

fun Analytics.logForYouPreferenceChanged(forYou: Boolean) =
    logPreferenceChanged("for_you", forYou.toString())

fun Analytics.logFabFiltersPreferenceChanged(fabFilters: Boolean) =
    logPreferenceChanged("fab_filters", fabFilters.toString())

fun Analytics.logCheckIntervalMinutesPreferenceChanged(checkIntervalMinutes: Int) =
    logPreferenceChanged("check_interval_minutes", checkIntervalMinutes.toString())

fun Analytics.logThemePreferenceChanged(themeName: String) =
    logPreferenceChanged("theme_name", themeName)

fun Analytics.logDynamicColorPreferenceChanged(useDynamicColor: Boolean) =
    logPreferenceChanged("dynamic_color", useDynamicColor.toString())


fun Analytics.logButtonClick(buttonId: String) = logEvent(
    AnalyticsEvent(
        type = types.BUTTON_CLICK,
        extras = listOf(Param(paramKeys.ITEM_ID, buttonId))
    )
)

fun Analytics.logNavigationAction(action: String, destination: String) = logEvent(
    AnalyticsEvent(
        type = types.SELECT_CONTENT,
        extras = listOf(
            Param(paramKeys.CONTENT_TYPE, "navigation_$action"),
            Param(paramKeys.ITEM_ID, destination)
        )
    )
)

fun Analytics.logContentLoadState(
    contentType: String,
    itemId: String,
    status: String,
    errorMessage: String? = null
) {
    val params = mutableListOf(
        Param(paramKeys.CONTENT_TYPE, contentType),
        Param(paramKeys.ITEM_ID, itemId),
        Param(paramKeys.STATUS, status)
    )

    if (errorMessage != null) {
        params.add(Param(paramKeys.ERROR_MESSAGE, errorMessage))
    }

    logEvent(AnalyticsEvent(type = types.CONTENT_LOAD_STATUS, extras = params))
}

fun Analytics.logItemSelection(itemId: String, category: String) = logEvent(
    AnalyticsEvent(
        type = types.SELECT_ITEM,
        extras = listOf(
            Param(paramKeys.ITEM_ID, itemId),
            Param(paramKeys.ITEM_CATEGORY, category)
        ),
    ),
)

fun Analytics.logSheetOpened(sheetName: String) = logEvent(
    AnalyticsEvent(
        type = types.BOTTOM_SHEET_OPENED,
        extras = listOf(Param(paramKeys.ITEM_ID, sheetName))
    )
)

fun Analytics.logFiltersApplied(filterCategory: String, appliedIds: List<Int>) = logEvent(
    AnalyticsEvent(
        type = types.FILTERS_APPLIED,
        extras = listOf(
            Param(paramKeys.ITEM_CATEGORY, filterCategory),
            Param(paramKeys.ITEM_ID, appliedIds.joinToString(","))
        ),
    ),
)

fun Analytics.logUserLogin() = logEvent(AnalyticsEvent(type = types.LOGIN))

fun Analytics.logUserLogout() = logEvent(AnalyticsEvent(type = types.LOGOUT))

fun Analytics.logSearch(
    query: String = "",
    authorIds: List<Int> = emptyList(),
    tagIds: List<Int> = emptyList()
) {
    val params = mutableListOf<Param>()

    if (query.isNotEmpty()) {
        params.add(Param(paramKeys.SEARCH_TERM, query))
    }
    if (authorIds.isNotEmpty()) {
        params.add(Param(paramKeys.AUTHOR_FILTERS, authorIds.joinToString(",")))
    }
    if (tagIds.isNotEmpty()) {
        params.add(Param(paramKeys.TAG_FILTERS, tagIds.joinToString(",")))
    }

    logEvent(
        AnalyticsEvent(
            type = types.SEARCH,
            extras = params
        )
    )
}