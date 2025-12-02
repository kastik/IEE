package com.kastik.apps.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import com.kastik.apps.core.model.aboard.UserProfile
import kotlinx.coroutines.delay


@Composable
internal fun ProfileRoute(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    TrackScreenViewEvent("profile_screen")

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is UiState.Loading -> ProfileScreenLoadingContent()
        is UiState.Error -> ProfileScreenErrorContent(error = state.message)
        is UiState.Success -> ProfileScreenSuccessContent(
            uiState = state,
            applySelectedTags = viewModel::onApplyTags,
            updateSelectedSubscribableTag = viewModel::updateSelectedTagIds,
            dismissSubscribeSheet = viewModel::toggleTagsSheet
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreenLoadingContent() {
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
private fun ProfileScreenErrorContent(error: String) {
    Surface {
        Box(
            Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(error)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenSuccessContent(
    uiState: UiState.Success,
    applySelectedTags: () -> Unit,
    updateSelectedSubscribableTag: (List<Int>) -> Unit,
    dismissSubscribeSheet: () -> Unit,
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
        }
    ) { innerPadding ->


        if (uiState.showTagSheet) {
            uiState.subscribableTags?.let { tags ->
                FilterSheetRecursiveTag(
                    tags = tags,
                    applySelectedTags = applySelectedTags,
                    selectedRootIds = uiState.selectedSubscribableTagsIds,
                    updateSelectedTagsIds = updateSelectedSubscribableTag,
                    sheetState = sheetState,
                    onDismiss = dismissSubscribeSheet,
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
                name = uiState.name
            )
            ProfileName(
                name = uiState.name, email = uiState.email
            )
            ProfileSubscribedTags(
                subscribedTagTitles = uiState.subscribedTags.map { it.title },
                dismissSubscribeSheet = dismissSubscribeSheet
            )
            ProfileMeta(
                isAdmin = uiState.isAdmin,
                isAuthor = uiState.isAuthor,
                lastLogin = uiState.lastLogin,
                createdAt = uiState.createdAt
            )
        }
    }
}

@Preview
@Composable
fun ProfileSuccessPreview() {
    ProfileScreenSuccessContent(
        uiState = UiState.Success(
            name = "John Doe",
            email = "john.quincy.adams@examplepetstore.com",
            isAdmin = false,
            isAuthor = true,
            lastLogin = "2-2-2025",
            createdAt = "2-2-2025",
            subscribedTags = listOf(),
            subscribableTags = null,
            selectedSubscribableTagsIds = emptyList(),
        ),
        updateSelectedSubscribableTag = {},
        applySelectedTags = {},
        dismissSubscribeSheet = {},
    )
}

@Preview
@Composable
fun PreviewProfileScreenLoading() {
    ProfileScreenLoadingContent()
}

@Preview
@Composable
fun PreviewProfileScreenError() {
    ProfileScreenErrorContent("Something Went Wrong")
}
