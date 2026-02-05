package com.kastik.apps.feature.announcement

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kastik.apps.core.common.extensions.shareAnnouncement
import com.kastik.apps.core.designsystem.component.IEEDotDivider
import com.kastik.apps.core.designsystem.component.IEETag
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.extensions.TrackAnnouncementOpened
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.core.ui.placeholder.StatusContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AnnouncementRoute(
    announcementId: Int,
    navigateBack: () -> Unit,
    viewModel: AnnouncementScreenViewModel = hiltViewModel(),
) {
    TrackScreenViewEvent("announcement_screen")
    TrackAnnouncementOpened(announcementId)

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState.value,
    ) { state ->
        when (state) {
            is UiState.Loading -> LoadingContent(
                modifier = Modifier.fillMaxSize(),
                message = "Fetching announcement...",
            )

            is UiState.Error -> StatusContent(message = state.message)

            is UiState.Success -> SuccessState(
                announcementId = state.announcement.id,
                title = state.announcement.title,
                author = state.announcement.author,
                date = state.announcement.date,
                prossedBodies = state.processedBody,
                tags = state.announcement.tags.toImmutableList(),
                attachments = state.announcement.attachments.toImmutableList(),
                navigateBack = navigateBack,
                onAttachmentClick = viewModel::downloadAttachment
            )
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
    prossedBodies: ImmutableList<ProcessedBody>,
    tags: ImmutableList<Tag>,
    attachments: ImmutableList<Attachment>,
    onAttachmentClick: (Int, Int, String, String) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val analytics = LocalAnalytics.current
    val scroll = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(scroll), verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = AnnotatedString.fromHtml(title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                ), color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                onClick = { context.shareAnnouncement(announcementId) }
            ) {
                Icon(
                    Icons.Outlined.Share,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

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

            IEEDotDivider()

            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (prossedBodies.isNotEmpty()) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    prossedBodies.forEach { part ->
                        when (part) {
                            is ProcessedBody.Text -> HtmlText(part)
                            is ProcessedBody.Image -> HtmlImage(part)
                        }
                    }
                }
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
                        analytics.logEvent(
                            "attachment_clicked",
                            mapOf("attachment_id" to attachment.id)
                        )
                        onAttachmentClick(
                            announcementId,
                            attachment.id,
                            attachment.filename,
                            attachment.mimeType,
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
                    IEETag(text = tag.title)
                }
            }
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}


@Composable
private fun HtmlText(text: ProcessedBody.Text) {
    SelectionContainer {
        Text(
            text = text.text,
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun HtmlImage(image: ProcessedBody.Image) {
    AsyncImage(
        model = image.url,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.FillWidth
    )
}

@Preview
@Composable
fun SuccessStatePreview() {
    SuccessState(
        announcementId = 1,
        title = "Announcement Title",
        author = "Kostas Papastathopoulos",
        prossedBodies = persistentListOf(ProcessedBody.Text(AnnotatedString.fromHtml("Announcement Body"))),
        date = "2/10/2025",
        tags = persistentListOf(
            Tag(id = 1, title = "Tag 1", false),
            Tag(id = 2, title = "Tag 3", false),
            Tag(id = 3, title = "Tag 2", false),
        ),
        attachments = persistentListOf(
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
        onAttachmentClick = { _, _, _, _ ->
        })
}