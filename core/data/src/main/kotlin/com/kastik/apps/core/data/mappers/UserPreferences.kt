package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.datastore.proto.UserPreferencesProto
import com.kastik.apps.core.model.user.UserPreferences

fun UserPreferencesProto.toUserPreferences() =
    UserPreferences(
        theme = theme.toTheme(),
        isDynamicColorEnabled = isDynamicColorEnabled,
        isForYouEnabled = isForYouEnabled,
        sortType = sortType.toSortType(),
        searchScope = searchScope.toSearchScope(),
        areFabFiltersEnabled = areFabFiltersEnabled,
        checkIntervalMinutes = checkIntervalMinutes,
        hasSkippedSignedIn = hasSkippedSignIn,
        importantEventCount = importantEventCount,
        lastNotificationCheckTime = lastCheckTime.toInstant(),
    )
