package com.kastik.appsaboard.ui.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    LazyColumn {
        items(viewModel.announcements.value) {
            Row {
                Text("Announcement: ${it.title}")
            }
        }
    }
}