package com.kastik.apps.feature.announcement

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import com.kastik.apps.core.model.aboard.AnnouncementView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnnouncementScreenViewModel @Inject constructor(
    private val getAnnouncementWithIdUseCase: GetAnnouncementWithIdUseCase
) : ViewModel() {


    val uiState: MutableState<UiState> = mutableStateOf(UiState.Loading)

    val data: MutableState<AnnouncementView?> = mutableStateOf(null)

    fun getData(id: Int) {
        viewModelScope.launch {
            uiState.value = UiState.Success(
                getAnnouncementWithIdUseCase(id)
            )
        }
    }
}