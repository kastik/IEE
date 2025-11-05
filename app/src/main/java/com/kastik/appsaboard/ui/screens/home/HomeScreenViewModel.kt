package com.kastik.appsaboard.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.appsaboard.domain.repository.AuthenticationRepository
import com.kastik.appsaboard.domain.usecases.GetAllAnnouncementsUseCase
import com.kastik.appsaboard.domain.usecases.GetPublicAnnouncementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPublicAnnouncementsUseCase: GetPublicAnnouncementsUseCase,
    private val getAllAnnouncementsUseCase: GetAllAnnouncementsUseCase,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    init {
        loadAnnouncements()
    }

    private val _uiState: MutableState<UiState> = mutableStateOf(UiState.Loading)
    val uiState: State<UiState> = _uiState


    fun loadAnnouncements() = viewModelScope.launch {
        runCatching {

            val announcements = if (authenticationRepository.getSavedToken().isNullOrEmpty()) {
                getPublicAnnouncementsUseCase()

            } else {
                getAllAnnouncementsUseCase()
            }
            _uiState.value = UiState.Success(data = announcements)
        }.onFailure { e ->
            _uiState.value = UiState.Error(e.message ?: "Unknown error")
        }
    }
}