package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.datastore.proto.Theme
import com.kastik.apps.core.model.user.UserTheme


fun Theme.toUserTheme(): UserTheme {
    return when (this) {
        Theme.System -> UserTheme.FOLLOW_SYSTEM
        Theme.Light -> UserTheme.LIGHT
        Theme.Dark -> UserTheme.DARK
        Theme.UNRECOGNIZED -> UserTheme.FOLLOW_SYSTEM
    }
}

fun UserTheme.toTheme(): Theme {
    return when (this) {
        UserTheme.FOLLOW_SYSTEM -> Theme.System
        UserTheme.LIGHT -> Theme.Light
        UserTheme.DARK -> Theme.Dark
    }
}
