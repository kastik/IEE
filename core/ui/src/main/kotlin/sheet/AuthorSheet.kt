package com.kastik.apps.core.ui.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kastik.apps.core.designsystem.component.IEESheet
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorSheet(
    authorSheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
    authors: ImmutableList<Author> = persistentListOf(),
    selectedAuthorIds: ImmutableList<Int> = persistentListOf(),
    onApply: (ImmutableList<Int>) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    IEESheet(
        sheetState = authorSheetState,
        items = authors,
        selectedIds = selectedAuthorIds,
        idProvider = { it.id },
        labelProvider = { author ->
            author.announcementCount?.let { announcementCount ->
                "${author.name} [${announcementCount}]"
            } ?: author.name
        },
        groupProvider = { it.name.first().uppercaseChar() },
        searchHint = stringResource(R.string.sheet_hint_author),
        applyLabel = stringResource(R.string.action_apply_authors),
        clearLabel = stringResource(R.string.action_clear),
        onApply = onApply,
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AuthorSheetPreview() {
    AppsAboardTheme {
        AuthorSheet(
            authors = persistentListOf(
                Author(id = 1, name = "Alice", announcementCount = 3),
                Author(id = 2, name = "Bob", announcementCount = 5),
                Author(id = 3, name = "Charlie", announcementCount = null),
                Author(id = 4, name = "David", announcementCount = 1),
            ), selectedAuthorIds = persistentListOf(1, 3)
        )
    }
}
