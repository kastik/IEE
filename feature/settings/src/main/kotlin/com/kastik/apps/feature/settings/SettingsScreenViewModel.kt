package com.kastik.apps.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.GetDynamicColorUseCase
import com.kastik.apps.core.domain.usecases.GetSearchScopeUseCase
import com.kastik.apps.core.domain.usecases.GetSortTypeUseCase
import com.kastik.apps.core.domain.usecases.GetUserThemeUseCase
import com.kastik.apps.core.domain.usecases.SetDynamicColorUseCase
import com.kastik.apps.core.domain.usecases.SetSearchScopeUseCase
import com.kastik.apps.core.domain.usecases.SetSortTypeUseCase
import com.kastik.apps.core.domain.usecases.SetUserThemeUseCase
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
    private val setSearchScopeUseCase: SetSearchScopeUseCase,
    private val setDynamicColorUseCase: SetDynamicColorUseCase,
    private val setUserThemeUseCase: SetUserThemeUseCase,
    private val setSortTypeUseCase: SetSortTypeUseCase,
    getSearchScopeUseCase: GetSearchScopeUseCase,
    getDynamicColorUseCase: GetDynamicColorUseCase,
    getUserThemeUseCase: GetUserThemeUseCase,
    getSortTypeUseCase: GetSortTypeUseCase,
) : ViewModel() {
    val uiState: StateFlow<UiState> = combine(
        getUserThemeUseCase(),
        getDynamicColorUseCase(),
        getSortTypeUseCase(),
        getSearchScopeUseCase(),
    ) { theme, dynamicColor, sortType, searchScope ->
        UiState.Success(
            theme = theme,
            sortType = sortType,
            dynamicColor = dynamicColor,
            searchScope = searchScope
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
}