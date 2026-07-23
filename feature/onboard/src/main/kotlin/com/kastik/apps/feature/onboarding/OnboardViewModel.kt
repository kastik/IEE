package com.kastik.apps.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.AreNotificationsAllowedUseCase
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetOnboardStageUseCase
import com.kastik.apps.core.domain.usecases.GetUserPreferencesUseCase
import com.kastik.apps.core.domain.usecases.IsForYouAvailableUseCase
import com.kastik.apps.core.domain.usecases.SetAnnouncementCheckIntervalUseCase
import com.kastik.apps.core.domain.usecases.SetDynamicColorUseCase
import com.kastik.apps.core.domain.usecases.SetForYouEnabledUseCase
import com.kastik.apps.core.domain.usecases.SetHasFinishedOnboardUseCase
import com.kastik.apps.core.domain.usecases.SetHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.SetOnboardStageUseCase
import com.kastik.apps.core.domain.usecases.SetSearchScopeUseCase
import com.kastik.apps.core.domain.usecases.SetSortTypeUseCase
import com.kastik.apps.core.domain.usecases.SetThemeUseCase
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.onboard.OnboardStage
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class OnboardViewModel
@Inject
constructor(
    private val setHasFinishedOnboardUseCase: SetHasFinishedOnboardUseCase,
    private val setHasSkippedSignInUseCase: SetHasSkippedSignInUseCase,
    private val setAnnouncementCheckIntervalUseCase: SetAnnouncementCheckIntervalUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val setDynamicColorUseCase: SetDynamicColorUseCase,
    private val setSortTypeUseCase: SetSortTypeUseCase,
    private val setForYouEnabledUseCase: SetForYouEnabledUseCase,
    private val setSearchScopeUseCase: SetSearchScopeUseCase,
    private val setOnboardStageUseCase: SetOnboardStageUseCase,
    getOnboardStageUseCase: GetOnboardStageUseCase,
    getIsForYouAvailableUseCase: IsForYouAvailableUseCase,
    getIsSignedInUseCase: GetIsSignedInUseCase,
    areNotificationsAllowedUseCase: AreNotificationsAllowedUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
) : ViewModel() {

    val uiState: StateFlow<OnboardUiState> =
        combine(
                areNotificationsAllowedUseCase(),
                getIsSignedInUseCase(),
                getUserPreferencesUseCase(),
                getIsForYouAvailableUseCase(),
                getOnboardStageUseCase(),
            ) { areNotificationsAllowed, isSignedIn, preferences, isForYouAvailable, stage ->
                OnboardUiState.Success(
                    isSignedIn = isSignedIn,
                    areNotificationsAllowed = areNotificationsAllowed,
                    theme = preferences.theme,
                    isDynamicColorEnabled = preferences.isDynamicColorEnabled,
                    searchScope = preferences.searchScope,
                    isForYouEnabled = preferences.isForYouEnabled,
                    sortType = preferences.sortType,
                    isForYouAvailable = isForYouAvailable,
                    onboardStage = stage,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = OnboardUiState.Loading,
            )

    fun onSkipSignIn() {
        viewModelScope.launch {
            setHasSkippedSignInUseCase(true)
        }
    }

    fun enableNotifications(quietDurationMinutes: Int = 60) {
        viewModelScope.launch {
            setAnnouncementCheckIntervalUseCase(quietDurationMinutes)
        }
    }

    fun onThemeChange(theme: Theme) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }

    fun onDynamicColorChange(enabled: Boolean) {
        viewModelScope.launch {
            setDynamicColorUseCase(enabled)
        }
    }

    fun onSortTypeChange(sortType: SortType) {
        viewModelScope.launch {
            setSortTypeUseCase(sortType)
        }
    }

    fun onForYouChange(isForYouEnabled: Boolean) {
        viewModelScope.launch {
            setForYouEnabledUseCase(isForYouEnabled)
        }
    }

    fun onSearchScopeChange(searchScope: SearchScope) {
        viewModelScope.launch {
            setSearchScopeUseCase(searchScope)
        }
    }

    fun onFinishedOnboarding() {
        viewModelScope.launch {
            setHasFinishedOnboardUseCase(true)
        }
    }

    fun onStageChange(onboardStage: OnboardStage) {
        viewModelScope.launch {
            setOnboardStageUseCase(onboardStage)
        }
    }
}
