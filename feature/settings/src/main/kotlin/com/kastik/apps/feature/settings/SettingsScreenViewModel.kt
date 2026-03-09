package com.kastik.apps.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.AreNotificationsAllowedUseCase
import com.kastik.apps.core.domain.usecases.GetUserPreferencesUseCase
import com.kastik.apps.core.domain.usecases.IsAnnouncementCheckIntervalAvailableUseCase
import com.kastik.apps.core.domain.usecases.IsForYouAvailableUseCase
import com.kastik.apps.core.domain.usecases.SetAnnouncementCheckIntervalUseCase
import com.kastik.apps.core.domain.usecases.SetDynamicColorUseCase
import com.kastik.apps.core.domain.usecases.SetFabFiltersEnabledUseCase
import com.kastik.apps.core.domain.usecases.SetForYouEnabledUseCase
import com.kastik.apps.core.domain.usecases.SetSearchScopeUseCase
import com.kastik.apps.core.domain.usecases.SetSortTypeUseCase
import com.kastik.apps.core.domain.usecases.SetThemeUseCase
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    isForYouAvailableUseCase: IsForYouAvailableUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
    areNotificationsAllowedUseCase: AreNotificationsAllowedUseCase,
    isAnnouncementCheckIntervalAvailableUseCase: IsAnnouncementCheckIntervalAvailableUseCase,
    private val setSearchScopeUseCase: SetSearchScopeUseCase,
    private val setDynamicColorUseCase: SetDynamicColorUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val setSortTypeUseCase: SetSortTypeUseCase,
    private val setForYouEnabledUseCase: SetForYouEnabledUseCase,
    private val setFabFiltersEnabledUseCase: SetFabFiltersEnabledUseCase,
    private val setAnnouncementCheckIntervalUseCase: SetAnnouncementCheckIntervalUseCase,
) : ViewModel() {

    val uiState: StateFlow<UiState> = combine(
        getUserPreferencesUseCase(),
        isForYouAvailableUseCase(),
        areNotificationsAllowedUseCase(),
        isAnnouncementCheckIntervalAvailableUseCase(),
    ) { preferences, isForYouAvailable, areNotificationsAllowed, isAnnouncementCheckIntervalAvailable ->
        UiState.Success(
            theme = preferences.theme,
            sortType = preferences.sortType,
            isDynamicColorEnabled = preferences.dynamicColor,
            searchScope = preferences.searchScope,
            isForYouEnabled = preferences.enableForYou,
            isForYouAvailable = isForYouAvailable,
            areFabFiltersEnabled = preferences.disableFabFilters,
            isAnnouncementCheckIntervalAvailable = isAnnouncementCheckIntervalAvailable,
            announcementCheckIntervalHours = preferences.announcementCheckIntervalHours,
            areNotificationsAllowed = areNotificationsAllowed,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = UiState.Loading
    )

    fun setDynamicColor(value: Boolean) {
        viewModelScope.launch {
            setDynamicColorUseCase(value)
        }
    }

    fun setTheme(theme: UserTheme) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }

    fun setSortType(sortType: SortType) {
        viewModelScope.launch {
            setSortTypeUseCase(sortType)
        }
    }

    fun setSearchScope(searchScope: SearchScope) {
        viewModelScope.launch {
            setSearchScopeUseCase(searchScope)
        }
    }

    fun setEnableForYou(value: Boolean) {
        viewModelScope.launch {
            setForYouEnabledUseCase(value)
        }
    }

    fun setFabFilters(value: Boolean) {
        viewModelScope.launch {
            setFabFiltersEnabledUseCase(value)
        }
    }

    fun setAnnouncementCheckIntervalHours(hours: Int) {
        viewModelScope.launch {
            setAnnouncementCheckIntervalUseCase(hours)
        }
    }
}