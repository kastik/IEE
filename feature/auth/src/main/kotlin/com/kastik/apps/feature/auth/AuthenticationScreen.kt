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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.designsystem.utils.TrackScreenViewEvent
import kotlinx.coroutines.delay


@Composable
internal fun AuthenticationRoute(
    navigateBack: () -> Unit,
    code: String? = null,
    state: String? = null,
    error: String? = null,
    errorDescription: String? = null,
    viewModel: AuthenticationScreenViewModel = hiltViewModel()
) {

    TrackScreenViewEvent("auth_screen")

    LaunchedEffect(Unit) {
        viewModel.onAuthRedirect(code = code, error = error, errorDesc = errorDescription)
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is UiState.Loading -> LoadingState()
        is UiState.Success -> SuccessState(navigateBack = navigateBack)
        is UiState.Error -> ErrorState(error = state.message, navigateBack = navigateBack)
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoadingState() {
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ErrorState(
    error: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1000)
        navigateBack()
    }

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(error)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SuccessState(
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        navigateBack()
    }
}


@Preview
@Composable
fun AuthenticationScreenPreview() {
    LoadingState()
}

