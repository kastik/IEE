package com.kastik.apps.core.ui.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kastik.apps.core.designsystem.component.IEESheet
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagSheet(
    tagSheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
    tags: ImmutableList<Tag> = persistentListOf(),
    selectedTagIds: ImmutableList<Int> = persistentListOf(),
    onApply: (ImmutableList<Int>) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    IEESheet(
        sheetState = tagSheetState,
        items = tags,
        selectedIds = selectedTagIds,
        idProvider = { it.id },
        labelProvider = { it.title },
        searchHint = stringResource(R.string.sheet_hint_tag),
        applyLabel = stringResource(R.string.action_apply_tags),
        clearLabel = stringResource(R.string.action_clear),
        onApply = onApply,
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TagSheetPreview() {
    val sampleTags = persistentListOf(
        Tag(id = 1, title = "Tag 1", isPublic = true),
        Tag(id = 2, title = "Tag 2", isPublic = true),
        Tag(id = 3, title = "Tag 3", isPublic = true),
    )
    AppsAboardTheme {
        TagSheet(
            tags = sampleTags,
            selectedTagIds = persistentListOf(1)
        )
    }
}
