package com.kastik.apps.core.ui.extensions

import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.analytics.AnalyticsEvent
import com.kastik.apps.core.analytics.AnalyticsEvent.Param

fun Analytics.logAnnouncementShared(announcementId: Int) =
    logEvent(
        AnalyticsEvent(
            type = types.share,
            extras =
                listOf(
                    Param(paramKeys.itemId, announcementId.toString()),
                    Param(paramKeys.contentType, "announcement"),
                ),
        )
    )

private fun Analytics.logPreferenceChanged(preferenceName: String, value: String) =
    logEvent(
        AnalyticsEvent(
            type = types.preferencesUpdated,
            extras =
                listOf(
                    Param(paramKeys.itemId, preferenceName),
                    Param(paramKeys.preferenceValue, value),
                ),
        )
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

fun Analytics.logButtonClick(buttonId: String) =
    logEvent(
        AnalyticsEvent(
            type = types.buttonClick,
            extras = listOf(Param(paramKeys.itemId, buttonId)),
        )
    )

fun Analytics.logNavigationAction(action: String, destination: String) =
    logEvent(
        AnalyticsEvent(
            type = types.selectContent,
            extras =
                listOf(
                    Param(paramKeys.contentType, "navigation_$action"),
                    Param(paramKeys.itemId, destination),
                ),
        )
    )

fun Analytics.logContentLoadState(
    contentType: String,
    itemId: String,
    status: String,
    errorMessage: String? = null,
) {
    val params =
        mutableListOf(
            Param(paramKeys.contentType, contentType),
            Param(paramKeys.itemId, itemId),
            Param(paramKeys.status, status),
        )

    if (errorMessage != null) {
        params.add(Param(paramKeys.errorMessage, errorMessage))
    }

    logEvent(AnalyticsEvent(type = types.contentLoadStatus, extras = params))
}

fun Analytics.logItemSelection(itemId: String, category: String) =
    logEvent(
        AnalyticsEvent(
            type = types.selectItem,
            extras =
                listOf(
                    Param(paramKeys.itemId, itemId),
                    Param(paramKeys.itemCategory, category),
                ),
        )
    )

fun Analytics.logSheetOpened(sheetName: String) =
    logEvent(
        AnalyticsEvent(
            type = types.bottomSheetOpened,
            extras = listOf(Param(paramKeys.itemId, sheetName)),
        )
    )

fun Analytics.logFiltersApplied(filterCategory: String, appliedIds: List<Int>) =
    logEvent(
        AnalyticsEvent(
            type = types.filtersApplied,
            extras =
                listOf(
                    Param(paramKeys.itemCategory, filterCategory),
                    Param(paramKeys.itemId, appliedIds.joinToString(",")),
                ),
        )
    )

fun Analytics.logUserLogin() = logEvent(AnalyticsEvent(type = types.login))

fun Analytics.logUserLogout() = logEvent(AnalyticsEvent(type = types.logout))

fun Analytics.logSearch(
    query: String = "",
    authorIds: List<Int> = emptyList(),
    tagIds: List<Int> = emptyList(),
) {
    val params = mutableListOf<Param>()

    if (query.isNotEmpty()) {
        params.add(Param(paramKeys.searchTerm, query))
    }
    if (authorIds.isNotEmpty()) {
        params.add(Param(paramKeys.authorFilters, authorIds.joinToString(",")))
    }
    if (tagIds.isNotEmpty()) {
        params.add(Param(paramKeys.tagFilters, tagIds.joinToString(",")))
    }

    logEvent(
        AnalyticsEvent(
            type = types.search,
            extras = params,
        )
    )
}
