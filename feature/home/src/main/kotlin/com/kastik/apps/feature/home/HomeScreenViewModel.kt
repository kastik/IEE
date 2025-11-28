package com.kastik.apps.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.domain.usecases.CheckIfUserHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.CheckIfUserIsAuthenticatedUseCase
import com.kastik.apps.core.domain.usecases.GetPagedAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.SetUserHasSkippedSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val analytics: Analytics,
    private val checkIfUserHasSkippedSignInUseCase: CheckIfUserHasSkippedSignInUseCase,
    private val checkIfUserIsAuthenticatedUseCase: CheckIfUserIsAuthenticatedUseCase,
    private val setUserHasSkippedSignInUseCase: SetUserHasSkippedSignInUseCase,
    private val getPagedAnnouncements: GetPagedAnnouncementsUseCase,
) : ViewModel() {

    init {
        evaluateSignInStatus()
    }

    private val _events = MutableSharedFlow<HomeEvent>(replay = 0)
    val events = _events.asSharedFlow()

    val announcements = getPagedAnnouncements().cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun onScreenViewed() {
        analytics.logScreenView("home_screen")
    }

    fun evaluateSignInStatus() {
        viewModelScope.launch {
            try {
                val isAuthenticated = checkIfUserIsAuthenticatedUseCase()
                val hasSkipped = checkIfUserHasSkippedSignInUseCase()
                _uiState.value = _uiState.value.copy(
                    isSignedIn = isAuthenticated,
                    showSignInNotice = !isAuthenticated && !hasSkipped,
                    hasEvaluatedAuth = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    hasEvaluatedAuth = false
                )
            }
        }
    }

    fun onSignInNoticeDismiss() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showSignInNotice = false)
            setUserHasSkippedSignInUseCase(true)
        }
    }

    fun onSignInClick() {
        val url =
            "https://login.it.teithe.gr/authorization?" + "client_id=690a9861468c9b767cabdc40" + "&response_type=code" + "&scope=announcements,profile" + "&redirect_uri=com.kastik.apps://auth"
        viewModelScope.launch {
            _events.emit(HomeEvent.OpenUrl(url))
        }
    }
}