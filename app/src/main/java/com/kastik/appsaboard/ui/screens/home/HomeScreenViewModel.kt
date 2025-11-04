package com.kastik.appsaboard.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.appsaboard.domain.models.Announcement
import com.kastik.appsaboard.domain.usecases.announcements.GetPublicAnnouncementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPublicAnnouncementsUseCase: GetPublicAnnouncementsUseCase,
) : ViewModel() {
    val announcements = mutableStateOf<List<Announcement>>(emptyList())

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() = viewModelScope.launch {
        announcements.value = getPublicAnnouncementsUseCase()
    }

}