package com.kastik.apps.core.ui.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kastik.apps.core.designsystem.component.IEESheet
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribableTagSheet(
    subscribeTagSheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
    subscribableTags: ImmutableList<SubscribableTag>,
    subscribedTags: ImmutableList<Int>,
    onApply: (ImmutableList<Int>) -> Unit,
    onDismiss: () -> Unit,
) {
    IEESheet(
        searchHint = stringResource(R.string.sheet_hint_subscribe),
        applyLabel = stringResource(R.string.action_subscribe),
        clearLabel = stringResource(R.string.action_clear),
        items = subscribableTags,
        subscribedTags = subscribedTags,
        applySelectedTags = onApply,
        sheetState = subscribeTagSheetState,
        onDismiss = onDismiss,
        idProvider = { tag -> tag.id },
        labelProvider = { it.title },
        childrenProvider = { it.subTags },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SubscribableTagSheetPreview() {
    val sampleSubTags = listOf(
        SubscribableTag(
            id = 2,
            title = "Sub Tag 1",
            parentId = 1,
            isPublic = true,
            createdAt = "",
            updatedAt = null,
            deletedAt = null,
            mailListName = "sub_tag_1",
            subTags = emptyList()
        ),
        SubscribableTag(
            id = 3,
            title = "Sub Tag 2",
            parentId = 1,
            isPublic = true,
            createdAt = "",
            updatedAt = null,
            deletedAt = null,
            mailListName = "sub_tag_2",
            subTags = listOf(
                SubscribableTag(
                    id = 4,
                    title = "Sub Tag 3",
                    parentId = 3,
                    isPublic = true,
                    createdAt = "",
                    updatedAt = null,
                    deletedAt = null,
                    mailListName = "sub_tag_3",
                    subTags = emptyList()
                ), SubscribableTag(
                    id = 5,
                    title = "Sub Tag 4",
                    parentId = 3,
                    isPublic = true,
                    createdAt = "",
                    updatedAt = null,
                    deletedAt = null,
                    mailListName = "sub_tag_4",
                    subTags = emptyList()
                )
            )
        ),
        SubscribableTag(
            id = 6,
            title = "Sub Tag 5",
            parentId = 1,
            isPublic = true,
            createdAt = "",
            updatedAt = null,
            deletedAt = null,
            mailListName = "sub_tag_5",
            subTags = emptyList()
        )
    )
    val sampleTags = persistentListOf(
        SubscribableTag(
            id = 1,
            title = "Root Tag",
            parentId = null,
            isPublic = true,
            createdAt = "",
            updatedAt = null,
            deletedAt = null,
            mailListName = "root_tag",
            subTags = sampleSubTags
        )
    )
    AppsAboardTheme {
        SubscribableTagSheet(
            subscribableTags = sampleTags,
            subscribedTags = persistentListOf(2, 4, 6),
            onApply = {},
            onDismiss = {}
        )
    }
}
