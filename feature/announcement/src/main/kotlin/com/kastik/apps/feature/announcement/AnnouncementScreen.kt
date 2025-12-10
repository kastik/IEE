package com.kastik.apps.feature.announcement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.designsystem.component.DotDivider
import com.kastik.apps.core.designsystem.component.FunkyChip
import com.kastik.apps.core.designsystem.utils.TrackScreenViewEvent
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.model.aboard.Tag

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AnnouncementRoute(
    announcementId: Int,
    navigateBack: () -> Unit,
    viewModel: AnnouncementScreenViewModel = hiltViewModel(),
) {

    TrackScreenViewEvent("announcement_screen")

    LaunchedEffect(Unit) {
        viewModel.getAnnouncement(announcementId)
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is UiState.Loading -> LoadingState()

        is UiState.Error -> ErrorState(state.message)

        is UiState.Success -> SuccessState(
            announcementId = state.announcement.id,
            title = state.announcement.title,
            author = state.announcement.author,
            date = state.announcement.date,
            body = state.announcement.body,
            tags = state.announcement.tags,
            attachments = state.announcement.attachments,
            navigateBack = navigateBack,
            onAttachmentClick = { attachmentId, filename ->
            }
        )

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoadingState() {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularWavyProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Text(text = "Fetching announcement...")
            }
        }
    }
}


@Composable
private fun ErrorState(message: String) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(message)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SuccessState(
    announcementId: Int,
    title: String,
    author: String,
    date: String,
    body: String,
    tags: List<Tag>,
    attachments: List<Attachment>,
    onAttachmentClick: (Int, String) -> Unit,
    navigateBack: () -> Unit,
) {
    val scroll = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                        ), color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scroll), verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                DotDivider()

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = AnnotatedString.fromHtml(body),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 24.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (attachments.isNotEmpty()) {
                Text(
                    text = "Attachments",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    attachments.forEach { attachment ->
                        AssistChip(onClick = {
                            onAttachmentClick(
                                attachment.id, attachment.filename
                            )
                        }, label = { Text(attachment.filename) }, leadingIcon = {
                            Icon(
                                Icons.Outlined.AttachFile,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        })
                    }
                }
            }

            if (tags.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    tags.forEach { tag ->
                        FunkyChip(text = tag.title)
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun SuccessStatePreview() {
    SuccessState(
        announcementId = 1,
        title = "Announcement Title",
        author = "Kostas Papastathopoulos",
        date = "2/10/2025",
        body = "The body of the announcement.",
        tags = listOf(
            Tag(id = 1, title = "Tag 1"),
            Tag(id = 2, title = "Tag 3"),
            Tag(id = 3, title = "Tag 2"),
        ),
        attachments = listOf(
            Attachment(
                id = 1, filename = "Attachment 1", fileSize = 1000, mimeType = "TODO()"
            ),
            Attachment(
                id = 2, filename = "Attachment 2", fileSize = 1000, mimeType = "TODO()"
            ),
            Attachment(
                id = 3, filename = "Attachment 3", fileSize = 1000, mimeType = "TODO()"
            ),
        ),
        navigateBack = {},
        onAttachmentClick = { _, _ ->
        })
}

@Preview
@Composable
fun ErrorStatePreview() {
    ErrorState("Something went wrong")
}

@Preview
@Composable
fun LoadingStatePreview() {
    LoadingState()
}