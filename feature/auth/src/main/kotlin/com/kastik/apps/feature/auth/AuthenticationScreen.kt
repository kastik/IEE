package com.kastik.apps.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.extensions.LocalAnalytics
import com.kastik.apps.core.designsystem.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.extensions.logContentLoadState
import com.kastik.apps.core.ui.extensions.logUserLogin
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.core.ui.placeholder.StatusContent
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun AuthenticationRoute(
    navigateBack: () -> Unit,
    viewModel: AuthenticationScreenViewModel = hiltViewModel()
) {
    TrackScreenViewEvent(
        screenClass = "authentication_route",
        screenName = "authentication_screen"
    )

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState.value,
        contentKey = { state -> state::class }
    ) { state ->
        when (state) {
            is AuthenticationUiState.Loading -> {
                AuthenticationLoading()
            }

            is AuthenticationUiState.Error -> {
                AuthenticationError(
                    navigateBack = navigateBack
                )
            }

            is AuthenticationUiState.Success -> {
                AuthenticationSuccess(
                    navigateBack = navigateBack
                )
            }
        }
    }
}

@Composable
private fun AuthenticationLoading(

) {
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logContentLoadState("authentication", "login", "loading")
    }

    LoadingContent(
        modifier = Modifier.fillMaxSize(),
        message = stringResource(R.string.sign_in_message),
    )
}

@Composable
private fun AuthenticationError(
    navigateBack: () -> Unit = {},
) {
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logContentLoadState("authentication", "login", "error")
        delay(2.seconds)
        navigateBack()
    }

    StatusContent(
        message = {
            Text(
                text = stringResource(R.string.error_generic),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}

@Composable
private fun AuthenticationSuccess(
    navigateBack: () -> Unit = {},
) {
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logUserLogin()
        analytics.logContentLoadState("authentication", "login", "success")
        delay(2.seconds)
        navigateBack()
    }

    StatusContent(
        message = {
            Text(
                text = stringResource(R.string.sign_in_success),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}


@Preview
@Composable
private fun AuthenticationLoadingPreview() {
    IeePreview {
        AuthenticationLoading()
    }
}

@Preview
@Composable
private fun AuthenticationErrorPreview() {
    IeePreview {
        AuthenticationError()
    }
}

@Preview
@Composable
private fun AuthenticationSuccessPreview() {
    IeePreview {
        AuthenticationSuccess()
    }
}