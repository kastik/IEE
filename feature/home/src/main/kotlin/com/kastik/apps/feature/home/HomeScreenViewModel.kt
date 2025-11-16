package com.kastik.apps.feature.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.domain.usecases.CheckIfUserHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.CheckIfUserIsAuthenticatedUseCase
import com.kastik.apps.core.domain.usecases.GetPagedAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.SetUserHasSkippedSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val checkIfUserHasSkippedSignInUseCase: CheckIfUserHasSkippedSignInUseCase,
    val checkIfUserIsAuthenticatedUseCase: CheckIfUserIsAuthenticatedUseCase,
    val setUserHasSkippedSignInUseCase: SetUserHasSkippedSignInUseCase,
    getPagedAnnouncements: GetPagedAnnouncementsUseCase,
) : ViewModel() {

    private val _announcements = getPagedAnnouncements().cachedIn(viewModelScope)
    val announcements = _announcements

    init {
        evaluateSignInStatus()
    }

    private val _uiState = mutableStateOf(UiState())
    val uiState: State<UiState> = _uiState

    fun evaluateSignInStatus() {
        viewModelScope.launch {
            val isAuthenticated = checkIfUserIsAuthenticatedUseCase()
            val hasSkipped = checkIfUserHasSkippedSignInUseCase()

            _uiState.value = _uiState.value.copy(
                isSignedIn = isAuthenticated,
                showSignInNotice = !isAuthenticated && !hasSkipped,
                hasEvaluatedAuth = true
            )
        }
    }

    fun onSignInNoticeDismissed() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showSignInNotice = false)
            setUserHasSkippedSignInUseCase(true)
        }
    }

    fun onSignInClicked() {
        //TODO
    }

}