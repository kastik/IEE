package com.kastik.apps.feature.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.designsystem.component.IEETag
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.core.ui.placeholder.StatusContent
import com.kastik.apps.core.ui.sheet.GenericRecursiveSheet
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList


@Composable
internal fun ProfileRoute(
    navigateBack: () -> Unit,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
) {
    TrackScreenViewEvent("profile_screen")

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState.value,
        contentKey = { state ->
            state::class
        }
    ) { state ->
        when (state) {
            is UiState.Loading -> LoadingContent(
                modifier = Modifier.fillMaxSize(),
                message = state.message,
            )
            is UiState.Error -> StatusContent(message = state.message)
            is UiState.SignedOut -> StatusContent(
                message = state.message,
                automaticAction = navigateBack
            )

            is UiState.Success -> SuccessState(
                uiState = state,
                applySelectedTags = viewModel::onApplyTags,
                updateSelectedSubscribableTag = viewModel::updateSelectedTagIds,
                showTagSheet = viewModel::toggleTagsSheet,
                onSignOutClick = viewModel::onSignOutClick
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessState(
    uiState: UiState.Success,
    applySelectedTags: () -> Unit,
    updateSelectedSubscribableTag: (ImmutableList<Int>) -> Unit,
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
                GenericRecursiveSheet(
                    items = tags.toImmutableList(),
                    applySelectedTags = applySelectedTags,
                    selectedRootIds = persistentListOf(),
                    updateSelectedTagsIds = updateSelectedSubscribableTag,
                    sheetState = sheetState,
                    onDismiss = { showTagSheet(false) },
                    idProvider = { tag -> tag.id },
                    labelProvider = { it.title },
                    childrenProvider = { it.subTags }
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
                subscribedTagTitles = uiState.subscribedTags.map { it.title }.toImmutableList(),
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


@Composable
private fun ProfileMeta(
    modifier: Modifier = Modifier,
    isAdmin: Boolean,
    isAuthor: Boolean,
    lastLogin: String,
    createdAt: String,
) {
    ElevatedCard(
        shape = RoundedCornerShape(22.dp), modifier = modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.ManageAccounts, null)
                Spacer(Modifier.width(8.dp))
                Text("Account Info", style = MaterialTheme.typography.titleMedium)
            }
            Column {
                Text(
                    "Role",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    when {
                        isAdmin -> "Administrator"
                        isAuthor -> "Author"
                        else -> "Student"
                    }, style = MaterialTheme.typography.bodyLarge
                )
            }
            Column {
                Text(
                    "Last Login",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    lastLogin, style = MaterialTheme.typography.bodyLarge
                )
            }
            Column {
                Text(
                    "Joined",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    createdAt, style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun ProfileName(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(name, style = MaterialTheme.typography.headlineSmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Email, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(email, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ProfilePicture(
    name: String
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .size(110.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.take(1).uppercase(),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun ProfileSubscribedTags(
    subscribedTagTitles: ImmutableList<String>,
    showTagSheet: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.NotificationsActive, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Subscribed Tags", style = MaterialTheme.typography.titleMedium)
                }
                IconButton(
                    onClick = { showTagSheet(true) }
                ) {
                    Icon(Icons.Outlined.Settings, null)
                }
            }

            if (subscribedTagTitles.isEmpty()) {
                Text(
                    "You haven’t subscribed to any tags yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    subscribedTagTitles.forEach { tag ->
                        IEETag(
                            tag
                        )
                    }
                }
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

            subscribedTags = persistentListOf(),
            subscribableTags = null,
            selectedSubscribableTagsIds = persistentListOf(),
        ),
        updateSelectedSubscribableTag = {},
        applySelectedTags = {},
        showTagSheet = {},
        onSignOutClick = {},
    )
}
