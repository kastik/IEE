package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.model.aboard.AnnouncementTag

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable

fun FilterSheetTag(
    tags: List<AnnouncementTag>,
    selectedIds: List<Int>,
    updateSelectedTagsIds: (List<Int>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
) {
    var tagQuery by remember { mutableStateOf("") }
    val selectedTagIds = remember { mutableStateListOf<Int>().apply { addAll(selectedIds) } }

    val filteredTags = remember(tagQuery, tags) {
        if (tagQuery.isBlank()) tags
        else tags.filter { it.title.contains(tagQuery, ignoreCase = true) }
    }

    val listState = rememberLazyListState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        OutlinedTextField(
            value = tagQuery,
            onValueChange = { tagQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search tags...") },
            singleLine = true
        )


        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(filteredTags) { tag ->
                val isSelected = tag.id in selectedTagIds
                SelectableTagItem(
                    title = tag.title,
                    isSelected = isSelected,
                    onToggle = {
                        if (isSelected) selectedTagIds.remove(tag.id)
                        else selectedTagIds.add(tag.id)
                    }
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                updateSelectedTagsIds(selectedTagIds)
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Apply Tags")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewFullFilterSheetTag() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    FilterSheetTag(
        tags = previewTags,
        selectedIds = listOf(1, 3, 4),
        updateSelectedTagsIds = {},
        sheetState = sheetState,
        onDismiss = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewHalfFilterSheetTag() {
    val sheetState = rememberModalBottomSheetState()
    FilterSheetTag(
        tags = previewTags,
        selectedIds = listOf(1, 3, 4),
        updateSelectedTagsIds = {},
        sheetState = sheetState,
        onDismiss = {}
    )
}

val previewTags = listOf(
    AnnouncementTag(
        id = 1,
        title = "Tag 1"
    ),
    AnnouncementTag(
        id = 2,
        title = "Tag 2"
    ),
    AnnouncementTag(
        id = 3,
        title = "Tag 3"
    ),
    AnnouncementTag(
        id = 4,
        title = "Tag 4"
    ),
    AnnouncementTag(
        id = 5,
        title = "Tag 5"
    ),
    AnnouncementTag(
        id = 6,
        title = "Tag 6"
    ),
    AnnouncementTag(
        id = 7,
        title = "Tag 7"
    ),
    AnnouncementTag(
        id = 8,
        title = "Tag 8"
    )
)


