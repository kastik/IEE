package com.kastik.apps.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.core.ui.placeholder.StatusContent


@Composable
internal fun AuthenticationRoute(
    navigateBack: () -> Unit,
    viewModel: AuthenticationScreenViewModel = hiltViewModel()
) {

    TrackScreenViewEvent("auth_screen")

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState.value,
    ) { state ->
        when (state) {
            is UiState.Loading -> LoadingContent(
                modifier = Modifier.fillMaxSize(),
                message = stringResource(R.string.sign_in_message),
            )

            is UiState.LocalError -> StatusContent(
                message = stringResource(state.resId),
                automaticAction = navigateBack
            )

            is UiState.ServerError -> StatusContent(
                message = state.message,
                automaticAction = navigateBack
            )

            is UiState.Success -> SuccessState(navigateBack = navigateBack)
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

