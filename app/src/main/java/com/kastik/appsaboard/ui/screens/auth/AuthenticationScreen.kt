package com.kastik.appsaboard.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kastik.appsaboard.ui.navigation.AuthRoute
import kotlinx.coroutines.delay

@Composable
fun AuthenticationScreen(
    navigateBack: () -> Unit,
    arguments: AuthRoute
) {
    val viewModel: AuthenticationScreenViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    LaunchedEffect(arguments.code, arguments.error) {
        viewModel.onAuthRedirect(arguments.code, arguments.error, arguments.error_description)
    }

    LaunchedEffect(uiState.value) {
        delay(500)
        when (uiState.value) {
            is UiState.Success -> {
                navigateBack()
            }

            is UiState.Error -> {
                navigateBack()
            }

            is UiState.Loading -> {

            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Spacer(Modifier.height(12.dp))
            Text("Signing you in")
        }
    }
}

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    data class Error(val message: String) : UiState()
}