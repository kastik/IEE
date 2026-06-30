package com.kastik.apps.feature.auth

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
            is UiState.Loading -> {
                AuthenticationLoading()
            }

            is UiState.LocalError -> {
                AuthenticationLocalError(
                    message = state.resId,
                    navigateBack = navigateBack
                )
            }

            is UiState.ServerError -> {
                AuthenticationServerError(
                    message = state.message,
                    navigateBack = navigateBack
                )
            }

            is UiState.Success -> {
                AuthenticationSuccess(navigateBack = navigateBack)
            }
        }
    }
}

@Composable
private fun AuthenticationLoading() {
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
private fun AuthenticationSuccess(
    navigateBack: () -> Unit = {},
) {
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logUserLogin()
        analytics.logContentLoadState("authentication", "login", "success")
        navigateBack()
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
private fun AuthenticationLocalError(
    @StringRes message: Int = 0,
    navigateBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val analytics = LocalAnalytics.current

    LaunchedEffect(message) {
        val errorMessage = context.getString(message)
        analytics.logContentLoadState("authentication", "login", "error", errorMessage)
    }

    StatusContent(
        message = stringResource(message),
        automaticAction = navigateBack
    )
}

@Composable
private fun AuthenticationServerError(
    message: String = "",
    navigateBack: () -> Unit = {},
) {
    val analytics = LocalAnalytics.current

    LaunchedEffect(message) {
        analytics.logContentLoadState("authentication", "login", "error", message)
    }

    StatusContent(
        message = message,
        automaticAction = navigateBack
    )
}

@Preview
@Composable
fun AuthenticationLoadingPreview() {
    IeePreview {
        AuthenticationLoading()
    }
}

@Preview
@Composable
fun AuthenticationSuccessPreview() {
    IeePreview {
        AuthenticationSuccess()
    }
}

@Preview
@Composable
fun AuthenticationLocalErrorPreview() {
    IeePreview {
        AuthenticationLocalError()
    }
}

@Preview
@Composable
fun AuthenticationServerErrorPreview() {
    IeePreview {
        AuthenticationServerError()
    }
}