package com.kastik.apps.feature.settings

import androidx.lifecycle.ViewModel
import com.kastik.apps.core.analytics.Analytics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val analytics: Analytics
) : ViewModel() {

    fun onScreenViewed() {
        analytics.logScreenView("settings_screen")
    }

}