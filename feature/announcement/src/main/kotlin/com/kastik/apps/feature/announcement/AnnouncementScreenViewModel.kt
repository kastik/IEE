package com.kastik.apps.feature.announcement

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnnouncementScreenViewModel @Inject constructor(
    private val getAnnouncementWithIdUseCase: GetAnnouncementWithIdUseCase,
    private val analytics: Analytics
) : ViewModel() {

    val uiState: MutableState<UiState> = mutableStateOf(UiState.Loading)
    fun onScreenViewed() {
        analytics.logScreenView("announcement_screen")
    }


    fun getData(id: Int) {
        viewModelScope.launch {
            try {
                getAnnouncementWithIdUseCase(id)
                    .collect { announcement ->
                        uiState.value = UiState.Success(announcement)
                    }
            } catch (e: Exception) {
                uiState.value = UiState.Error
            }

        }
    }
}