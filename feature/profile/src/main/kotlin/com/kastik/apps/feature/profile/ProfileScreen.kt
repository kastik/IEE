package com.kastik.apps.feature.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.designsystem.component.IeeLinearWavyProgressIndicator
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.component.IeeStatusBanner
import com.kastik.apps.core.designsystem.component.IeeTag
import com.kastik.apps.core.designsystem.extensions.LocalAnalytics
import com.kastik.apps.core.designsystem.extensions.TrackScreenViewEvent
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.extensions.logButtonClick
import com.kastik.apps.core.ui.extensions.logFiltersApplied
import com.kastik.apps.core.ui.extensions.logSheetOpened
import com.kastik.apps.core.ui.extensions.logUserLogout
import com.kastik.apps.core.ui.extensions.toFormattedString
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.core.ui.placeholder.StatusContent
import com.kastik.apps.core.ui.sheet.SubscribableTagSheet
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
internal fun ProfileRoute(
    navigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    TrackScreenViewEvent(
        screenClass = "profile_route",
        screenName = "profile_screen",
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState,
        contentKey = { state -> state::class }
    ) { state ->
        when (state) {

            is ProfileUiState.Loading -> {
                ProfileLoading()
            }

            is ProfileUiState.SignedOut -> {
                ProfileSignedOut(
                    navigateBack = navigateBack
                )
            }

            is ProfileUiState.Success -> {
                ProfileSuccess(
                    name = state.profile.name,
                    email = state.profile.email,
                    isAdmin = state.profile.isAdmin,
                    isAuthor = state.profile.isAuthor,
                    lastLoginAt = state.profile.lastLoginAt.toFormattedString(),
                    createdAt = state.profile.createdAt.toFormattedString(),
                    isSyncingSubscriptions = state.isSyncingSubscriptions,
                    subscriptionSyncError = state.subscribeSyncErrorMessageResId?.let {
                        stringResource(
                            it
                        )
                    },
                    subscribableTags = state.subscribableTags,
                    subscribedTags = state.subscribedTags,
                    applySelectedTags = viewModel::updateSelectedTagIds,
                    isSubscribeSheetVisible = state.isSubscribeSheetVisible,
                    toggleSubscribeSheet = viewModel::toggleTagsSheet,
                    onSignOutClick = viewModel::onSignOutClick,
                )
            }
        }
    }
}

@Composable
private fun ProfileLoading(
) {
    LoadingContent(
        modifier = Modifier.fillMaxSize(),
        message = stringResource(R.string.fetching_profile_message),
    )
}


@Composable
private fun ProfileSignedOut(
    navigateBack: () -> Unit = {},
) {
    val analytics = LocalAnalytics.current

    LaunchedEffect(Unit) {
        analytics.logUserLogout()
        delay(1.seconds)
        navigateBack()
    }

    StatusContent(
        modifier = Modifier.fillMaxSize(),
        message = {
            Text(
                text = stringResource(R.string.logged_out_message),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ProfileSuccess(
    name: String,
    email: String,
    isAdmin: Boolean = false,
    isAuthor: Boolean = false,
    lastLoginAt: String = "25/12/2026",
    createdAt: String = "25/12/2026",
    isSyncingSubscriptions: Boolean = false,
    subscriptionSyncError: String? = null,
    subscribedTags: ImmutableList<Tag>,
    subscribableTags: ImmutableList<Tag>,
    applySelectedTags: (ImmutableList<Int>) -> Unit = {},
    isSubscribeSheetVisible: Boolean = false,
    toggleSubscribeSheet: (Boolean) -> Unit = {},
    onSignOutClick: () -> Unit = {},
) {
    val analytics = LocalAnalytics.current

    Scaffold(
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->

        if (isSubscribeSheetVisible) {
            SubscribableTagSheet(
                tags = subscribableTags,
                subscribedTags = subscribedTags.map { it.id }.toImmutableList(),
                onApply = { newTagIds ->
                    analytics.logFiltersApplied("subscribed_tags", newTagIds)
                    applySelectedTags(newTagIds)
                },
                onDismiss = { toggleSubscribeSheet(false) }
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))


            AnimatedVisibility(
                visible = isSyncingSubscriptions && subscriptionSyncError == null,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IeeLinearWavyProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            AnimatedVisibility(
                visible = subscriptionSyncError != null,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                subscriptionSyncError?.let { error ->
                    IeeStatusBanner(
                        modifier = Modifier.fillMaxWidth(),
                        text = error,
                    )
                }
            }

            ProfilePicture(name = name)
            ProfileName(name = name, email = email)

            ProfileSubscribedTags(
                subscribedTagTitles = subscribedTags.map { it.title }.toImmutableList(),
                showTagSheet = { value ->
                    if (value) analytics.logSheetOpened("subscribed_tags_sheet")
                    toggleSubscribeSheet(value)
                }
            )

            ProfileMeta(
                isAdmin = isAdmin,
                isAuthor = isAuthor,
                lastLogin = lastLoginAt,
                createdAt = createdAt
            )

            ProfileSignOut(
                onClick = {
                    analytics.logButtonClick("sign_out")
                    onSignOutClick()
                }
            )
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
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
        shape = RoundedCornerShape(22.dp),
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

    ) {
        Column(
            Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.ManageAccounts, null)
                Spacer(Modifier.width(8.dp))
                Text(
                    stringResource(R.string.account_info_label),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.role_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    when {
                        isAdmin -> stringResource(R.string.role_admin_label)
                        isAuthor -> stringResource(R.string.role_author_label)
                        else -> stringResource(R.string.role_student_label)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.account_info_last_log_in),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = lastLogin,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.account_info_joined),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    createdAt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
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
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

    ) {
        Column(
            Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(name, style = MaterialTheme.typography.titleLarge)
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
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

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
                    Text(
                        stringResource(R.string.subscribed_tags_label),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                IconButton(
                    onClick = { showTagSheet(true) }) {
                    Icon(Icons.Outlined.Settings, null)
                }
            }

            AnimatedContent(
                targetState = subscribedTagTitles,
            ) { tags ->
                if (tags.isEmpty()) {
                    Text(
                        text = stringResource(R.string.warning_empty_subscriptions),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    FlowRow(
                        modifier = Modifier.animateContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tags.forEach { tag ->
                            IeeTag(
                                text = tag
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ProfileSignOut(
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(46.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
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
            text = stringResource(R.string.action_sign_out),
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@Preview
@Composable
internal fun ProfileLoadingPreview() {
    IeePreview {
        ProfileLoading()
    }
}

@Preview
@Composable
internal fun ProfileSignedOutPreview() {
    IeePreview {
        ProfileSignedOut()
    }
}


@Preview
@Composable
internal fun ProfileSuccessPreview() {
    IeePreview {
        ProfileSuccess(
            name = "Kostas Papastathopoulos",
            email = "example@gmail.com",
            subscribableTags = persistentListOf(),
            subscribedTags = persistentListOf()
        )
    }
}
