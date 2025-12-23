package com.kastik.apps.core.ui.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.kastik.apps.core.model.user.UserTheme

@Composable
fun UserTheme.shouldUseDarkTheme(): Boolean {
    return when (this) {
        UserTheme.DARK -> true
        UserTheme.LIGHT -> false
        UserTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    }
}