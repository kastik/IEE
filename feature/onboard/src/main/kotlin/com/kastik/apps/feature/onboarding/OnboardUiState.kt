package com.kastik.apps.feature.onboarding

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.onboard.OnboardStage
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme

sealed interface OnboardUiState {

    data object Loading : OnboardUiState

    data class Success(
        val isSignedIn: Boolean = false,
        val areNotificationsAllowed: Boolean = false,
        val theme: Theme = Theme.FOLLOW_SYSTEM,
        val isDynamicColorEnabled: Boolean = true,
        val isForYouAvailable: Boolean = false,
        val isForYouEnabled: Boolean = false,
        val sortType: SortType = SortType.DESC,
        val searchScope: SearchScope = SearchScope.Body,
        val onboardStage: OnboardStage = OnboardStage.Welcome,
    ) : OnboardUiState
}
