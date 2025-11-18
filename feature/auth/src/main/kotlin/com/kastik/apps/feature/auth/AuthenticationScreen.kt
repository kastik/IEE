package com.kastik.apps.feature.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.delay


@Composable
fun AuthenticationScreen(
    navigateBack: () -> Unit,
    code: String? = null,
    state: String? = null,
    error: String? = null,
    errorDescription: String? = null,
    viewModel: AuthenticationScreenViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
    LaunchedEffect(code, error) {
        viewModel.onAuthRedirect(code = code, error = error, errorDesc = errorDescription)
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

}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthenticationScreenContent() {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularWavyProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(Modifier.height(12.dp))
                Text("Signing you in")
            }
        }
    }
}


@Preview
@Composable
fun AuthenticationScreenPreview() {
    AuthenticationScreenContent()
}

