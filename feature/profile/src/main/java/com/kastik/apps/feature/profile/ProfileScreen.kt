package com.kastik.apps.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.designsystem.component.FilterSheetRecursiveTag
import com.kastik.apps.core.designsystem.component.ProfileMeta
import com.kastik.apps.core.designsystem.component.ProfileName
import com.kastik.apps.core.designsystem.component.ProfilePicture
import com.kastik.apps.core.designsystem.component.ProfileSubscribedTags
import com.kastik.apps.core.designsystem.utils.TrackScreenViewEvent
import com.kastik.apps.core.model.aboard.Profile
import kotlinx.coroutines.delay


@Composable
internal fun ProfileRoute(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    TrackScreenViewEvent("profile_screen")

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is UiState.Loading -> LoadingState()
        is UiState.Error -> ErrorState(error = state.message)
        is UiState.SignedOut -> LoggedOutState(state.message, navigateBack)
        is UiState.Success -> SuccessState(
            uiState = state,
            applySelectedTags = viewModel::onApplyTags,
            updateSelectedSubscribableTag = viewModel::updateSelectedTagIds,
            showTagSheet = viewModel::toggleTagsSheet,
            onSignOutClick = viewModel::onSignOutClick
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoadingState() {
    Surface {
        Box(
            Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularWavyProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@Composable
private fun ErrorState(error: String) {
    Surface {
        Box(
            Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(error)
        }
    }

}

@Composable
private fun LoggedOutState(
    message: String,
    navigateBack: () -> Unit
) {
    Surface {
        Box(
            Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(message)
            LaunchedEffect(Unit) {
                delay(1000)
                navigateBack()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessState(
    uiState: UiState.Success,
    applySelectedTags: () -> Unit,
    updateSelectedSubscribableTag: (List<Int>) -> Unit,
    showTagSheet: (Boolean) -> Unit,
    onSignOutClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Profile") },
                navigationIcon = {},
            )
        }) { innerPadding ->


        if (uiState.showTagSheet) {
            uiState.subscribableTags?.let { tags ->
                FilterSheetRecursiveTag(
                    tags = tags,
                    applySelectedTags = applySelectedTags,
                    selectedRootIds = uiState.selectedSubscribableTagsIds,
                    updateSelectedTagsIds = updateSelectedSubscribableTag,
                    sheetState = sheetState,
                    onDismiss = { showTagSheet(false) },
                )
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            ProfilePicture(
                name = uiState.profile.name
            )
            ProfileName(
                name = uiState.profile.name, email = uiState.profile.email
            )
            ProfileSubscribedTags(
                subscribedTagTitles = uiState.subscribedTags.map { it.title },
                showTagSheet = showTagSheet
            )
            ProfileMeta(
                isAdmin = uiState.profile.isAdmin,
                isAuthor = uiState.profile.isAuthor,
                lastLogin = uiState.profile.lastLoginAt,
                createdAt = uiState.profile.createdAt
            )
            Button(
                onClick = onSignOutClick,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .height(46.dp),
                shape = MaterialTheme.shapes.medium, // Expressive rounded shape
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log Out", style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileSuccessStatePreview() {
    SuccessState(
        uiState = UiState.Success(
            profile = Profile(
                id = 5,
                uid = "134234",
                name = "John Doe",
                email = "john.quincy.adams@examplepetstore.com",
                isAdmin = false,
                isAuthor = true,
                lastLoginAt = "2-2-2025",
                createdAt = "2-2-2025",
                updatedAt = "2-2-2025",
                deletedAt = null,
            ),

            subscribedTags = listOf(),
            subscribableTags = null,
            selectedSubscribableTagsIds = emptyList(),
        ),
        updateSelectedSubscribableTag = {},
        applySelectedTags = {},
        showTagSheet = {},
        onSignOutClick = {},
    )
}

@Preview
@Composable
fun PreviewLoadingState() {
    LoadingState()
}

@Preview
@Composable
fun PreviewErrorState() {
    ErrorState("Something Went Wrong")
}
