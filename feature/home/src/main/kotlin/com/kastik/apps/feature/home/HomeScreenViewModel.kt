package com.kastik.apps.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetPagedAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.SetUserHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.ShouldRefreshAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.ShowSignInNoticeRationalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val isSignedInUseCase: GetIsSignedInUseCase,
    private val getPagedAnnouncements: GetPagedAnnouncementsUseCase,
    private val setUserHasSkippedSignInUseCase: SetUserHasSkippedSignInUseCase,
    private val showSignInNoticeRationalUseCase: ShowSignInNoticeRationalUseCase,
    private val shouldRefreshAnnouncementsUseCase: ShouldRefreshAnnouncementsUseCase,
) : ViewModel() {
    val announcements = getPagedAnnouncements().cachedIn(viewModelScope)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _refreshTrigger = Channel<Unit>(Channel.BUFFERED)
    val refreshTrigger = _refreshTrigger.receiveAsFlow()


    init {
        viewModelScope.launch {
            combine(
                showSignInNoticeRationalUseCase(),
                isSignedInUseCase()
            ) { showSignInNotice, isSignedIn ->
                _uiState.update {
                    it.copy(
                        isSignedIn = isSignedIn,
                        showSignInNotice = showSignInNotice
                    )
                }
            }.stateIn(viewModelScope)
        }

        viewModelScope.launch {
            shouldRefreshAnnouncementsUseCase().collect {
                _refreshTrigger.send(Unit)
            }
        }
    }

    fun onSignInNoticeDismiss() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showSignInNotice = false)
            setUserHasSkippedSignInUseCase(true)
        }
    }
}