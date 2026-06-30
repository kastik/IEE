package com.kastik.apps.feature.announcement

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.kastik.apps.core.analytics.AnalyticsEvent
import com.kastik.apps.core.common.extensions.getEnglishString
import com.kastik.apps.core.common.extensions.launchInAppReview
import com.kastik.apps.core.common.extensions.shareAnnouncement
import com.kastik.apps.core.designsystem.component.IeeDotDivider
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.component.IeeTag
import com.kastik.apps.core.designsystem.extensions.LocalAnalytics
import com.kastik.apps.core.designsystem.extensions.TrackScreenViewEvent
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.extensions.logAnnouncementShared
import com.kastik.apps.core.ui.extensions.logButtonClick
import com.kastik.apps.core.ui.extensions.logContentLoadState
import com.kastik.apps.core.ui.extensions.logItemSelection
import com.kastik.apps.core.ui.extensions.logSheetOpened
import com.kastik.apps.core.ui.extensions.toFormattedString
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
    val analytics = LocalAnalytics.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    TrackScreenViewEvent(
        screenClass = "announcement_route",
        screenName = "announcement_screen",
        params = listOf(
            AnalyticsEvent.Param(analytics.paramKeys.ITEM_ID, announcementId.toString()),
            AnalyticsEvent.Param(analytics.paramKeys.ITEM_CATEGORY, "announcement")
        )
    )

    AnimatedContent(
        targetState = uiState.value,
        contentKey = { state -> state::class }
    ) { state ->
        when (state) {
            is UiState.Loading -> {
                AnnouncementScreenLoading(announcementId = announcementId)
            }

            is UiState.Error -> {
                AnnouncementScreenError(
                    resId = state.resId,
                    announcementId = announcementId,
                )
            }

            is UiState.Success -> {
                AnnouncementScreenSuccess(
                    announcementId = state.announcement.id,
                    title = state.announcement.title,
                    author = state.announcement.author,
                    date = state.announcement.date.toFormattedString(),
                    prossedBodies = state.processedBody,
                    tags = state.announcement.tags.toImmutableList(),
                    attachments = state.announcement.attachments.toImmutableList(),
                    shouldShowReviewDialog = state.shouldShowReviewDialog,
                    onSuccessfulReview = viewModel::onSuccessfulReview,
                    navigateBack = navigateBack,
                    onAttachmentClick = viewModel::downloadAttachment
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AnnouncementScreenSuccess(
    announcementId: Int,
    title: String,
    author: String,
    date: String,
    prossedBodies: ImmutableList<ProcessedBody>,
    tags: ImmutableList<Tag> = persistentListOf(),
    attachments: ImmutableList<Attachment> = persistentListOf(),
    onAttachmentClick: (Int, Int, String, String) -> Unit = { _, _, _, _ -> },
    shouldShowReviewDialog: Boolean = false,
    onSuccessfulReview: () -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val analytics = LocalAnalytics.current
    val scroll = rememberScrollState()

    LaunchedEffect(announcementId) {
        analytics.logContentLoadState("announcement", announcementId.toString(), "success")
    }

    LaunchedEffect(shouldShowReviewDialog) {
        if (shouldShowReviewDialog) {
            analytics.logSheetOpened("in_app_review_dialog")
            context.launchInAppReview(onSuccessfulReview = onSuccessfulReview)
        }
    }

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
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ), color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                onClick = {
                    analytics.logButtonClick("share_announcement_icon")
                    analytics.logAnnouncementShared(announcementId)
                    context.shareAnnouncement(announcementId)
                }
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
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            IeeDotDivider()

            Text(
                text = date,
                style = MaterialTheme.typography.labelLarge,
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
                text = stringResource(R.string.attachments_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                attachments.forEach { attachment ->
                    AssistChip(
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        onClick = {
                            analytics.logItemSelection(attachment.id.toString(), "attachment")
                            onAttachmentClick(
                                announcementId,
                                attachment.id,
                                attachment.fileName,
                                attachment.mimeType,
                            )
                        },
                        label = {
                            Text(
                                text = attachment.fileName,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.AttachFile,
                                contentDescription = attachment.fileName,
                            )
                        }
                    )
                }
            }
        }

        if (tags.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Text(
                text = stringResource(R.string.tags_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                tags.forEach { tag ->
                    IeeTag(text = tag.title)
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
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
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

@Composable
private fun AnnouncementScreenLoading(
    announcementId: Int = 0
) {
    val analytics = LocalAnalytics.current

    LaunchedEffect(announcementId) {
        analytics.logContentLoadState("announcement", announcementId.toString(), "loading")
    }

    LoadingContent(
        modifier = Modifier.fillMaxSize(),
        message = stringResource(R.string.fetching_message),
    )
}

@Composable
private fun AnnouncementScreenError(
    announcementId: Int,
    @StringRes resId: Int,
) {
    val context = LocalContext.current
    val analytics = LocalAnalytics.current

    LaunchedEffect(announcementId, resId) {
        val englishErrorMessage = context.getEnglishString(resId)

        analytics.logContentLoadState(
            contentType = "announcement",
            itemId = announcementId.toString(),
            status = "error",
            errorMessage = englishErrorMessage
        )
    }

    StatusContent(message = stringResource(resId))
}

@Preview
@Composable
fun AnnouncementScreenSuccessPreview() {
    IeePreview {
        AnnouncementScreenSuccess(
            announcementId = 0,
            title = "Announcement Title",
            author = "Author Name",
            date = "25-12-2026",
            prossedBodies = persistentListOf(
                ProcessedBody.Text(
                    AnnotatedString("The quick brown fox")
                )
            )
        )
    }
}

@Preview
@Composable
fun AnnouncementScreenLoadingPreview() {
    IeePreview {
        AnnouncementScreenLoading()
    }
}

@Preview
@Composable
fun AnnouncementScreenErrorPreview() {
    IeePreview {
        AnnouncementScreenError(
            resId = 0,
            announcementId = 0
        )
    }
}