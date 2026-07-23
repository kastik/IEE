package com.kastik.apps.core.ui.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.kastik.apps.core.model.user.Theme

@Composable
fun Theme.shouldUseDarkTheme(): Boolean {
    return when (this) {
        Theme.DARK -> true
        Theme.LIGHT -> false
        Theme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    }
}
