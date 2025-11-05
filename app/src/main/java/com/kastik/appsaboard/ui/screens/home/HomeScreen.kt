package com.kastik.appsaboard.ui.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kastik.appsaboard.domain.models.Announcement

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    when (val uiState = viewModel.uiState.value) {
        is UiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Spacer(Modifier.height(12.dp))
            Text("Fetching Announcements...")
        }

        is UiState.Success -> {
            LazyColumn {
                item {
                    Button(
                        modifier = Modifier.padding(100.dp),
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    "https://login.it.teithe.gr/authorization?" +
                                            "client_id=690a9861468c9b767cabdc40" +
                                            "&" +
                                            "response_type=code" +
                                            "&" +
                                            "scope=announcements,profile" +
                                            "&" +
                                            "redirect_uri=com.kastik.apps://auth"
                                )
                            )
                            context.startActivity(intent)
                        }
                    ) {
                        Text("Sign in")
                    }
                }
                items(uiState.data) {
                    Row {
                        Text("Announcement: ${it.title}")
                    }
                }
            }

        }

        is UiState.Error -> {
            Text("Error: ${uiState.message}")
        }
    }


}

sealed class UiState {
    data object Loading : UiState()
    data class Success(val data: List<Announcement>) : UiState()
    data class Error(val message: String) : UiState()
}