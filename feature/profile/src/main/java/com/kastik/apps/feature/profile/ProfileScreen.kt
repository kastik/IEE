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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kastik.apps.core.model.aboard.UserSubscribedTag


@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }

    val uiState = viewModel.uiState.value
    AnimatedContent(
        targetState = uiState,
    ) { state ->
        when (state) {
            is UiState.Loading -> ProfileScreenLoadingContent()
            is UiState.Error -> ProfileScreenErrorContent(error = state.message)
            is UiState.Success -> ProfileScreenSuccessContent(
                name = state.profile.name,
                email = state.profile.email,
                isAdmin = state.profile.isAdmin,
                isAuthor = state.profile.isAuthor,
                lastLogin = state.profile.lastLoginAt,
                createdAt = state.profile.createdAt,
                subscribedTags = state.subscribedTag
            )
        }

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreenLoadingContent() {
    Surface {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
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
fun ProfileScreenErrorContent(error: String) {
    Surface {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(error)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenSuccessContent(
    name: String,
    email: String,
    isAdmin: Boolean,
    isAuthor: Boolean,
    lastLogin: String,
    createdAt: String,
    subscribedTags: List<UserSubscribedTag>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Profile") },
                navigationIcon = {},
            )
        }
    ) { innerPadding ->

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {

            // Avatar
            Box(
                modifier = Modifier
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

            // Name + Email Card
            ElevatedCard(
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(name, style = MaterialTheme.typography.headlineSmall)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Email, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(email, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Subscribed Tag
            ElevatedCard(
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.NotificationsActive, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Subscribed Tags", style = MaterialTheme.typography.titleMedium)
                    }

                    if (subscribedTags.isEmpty()) {
                        Text(
                            "You haven’t subscribed to any tags yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        // Playful chip cloud
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            subscribedTags.forEachIndexed { index, tag ->
                                AssistChip(
                                    onClick = { /* maybe manage tags later */ },
                                    shape = MaterialTheme.shapes.medium,
                                    label = { Text(tag.title) },
                                    modifier = Modifier.wrapContentSize()
                                )
                            }
                        }
                    }
                }
            }

            // Account Meta
            ElevatedCard(
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.ManageAccounts, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Account Info", style = MaterialTheme.typography.titleMedium)
                    }

                    MetaText(
                        "Role", when {
                            isAdmin -> "Administrator"
                            isAuthor -> "Author"
                            else -> "Student"
                        }
                    )

                    MetaText("Last Login", lastLogin)
                    MetaText("Joined", createdAt)
                }
            }
        }


    }

}

@Composable
private fun MetaText(label: String, value: String) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

val FunkyShape = RoundedCornerShape(
    topStart = 28.dp,
    topEnd = 10.dp,
    bottomEnd = 32.dp,
    bottomStart = 16.dp
)

@Preview
@Composable
fun ProfileSuccessPreview() {
    ProfileScreenSuccessContent(
        name = "John Doe",
        email = "john.quincy.adams@examplepetstore.com",
        isAdmin = false,
        isAuthor = true,
        lastLogin = "2-2-2025",
        createdAt = "2-2-2025",
        subscribedTags = listOf()
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
