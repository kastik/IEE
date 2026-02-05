package com.kastik.apps.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.GetUserPreferencesUseCase
import com.kastik.apps.core.domain.usecases.SetDynamicColorUseCase
import com.kastik.apps.core.domain.usecases.SetEnableForYouUseCase
import com.kastik.apps.core.domain.usecases.SetSearchScopeUseCase
import com.kastik.apps.core.domain.usecases.SetSortTypeUseCase
import com.kastik.apps.core.domain.usecases.SetUserThemeUseCase
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val setSearchScopeUseCase: SetSearchScopeUseCase,
    private val setDynamicColorUseCase: SetDynamicColorUseCase,
    private val setUserThemeUseCase: SetUserThemeUseCase,
    private val setSortTypeUseCase: SetSortTypeUseCase,
    private val setEnableForYouUseCase: SetEnableForYouUseCase,
) : ViewModel() {

    private val errorState = MutableStateFlow<String?>(null)

    val uiState: StateFlow<UiState> = combine(
        errorState,
        getUserPreferencesUseCase(),
    ) { error, preferences ->
        UiState.Success(
            theme = preferences.theme,
            sortType = preferences.sortType,
            dynamicColor = preferences.dynamicColor,
            searchScope = preferences.searchScope,
            forYou = preferences.enableForYou
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
            setUserThemeUseCase(theme)
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
            setEnableForYouUseCase(value)
        }
    }
}