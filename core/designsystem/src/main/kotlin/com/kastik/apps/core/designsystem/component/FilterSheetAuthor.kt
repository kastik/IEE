package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
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
import com.kastik.apps.core.model.aboard.Author

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterSheetAuthor(
    authors: List<Author>,
    selectedIds: List<Int>,
    updateSelectedAuthorIds: (List<Int>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    var authorQuery by remember { mutableStateOf("") }

    val selectedAuthorIds = remember {
        mutableStateListOf<Int>().apply { addAll(selectedIds) }
    }

    val filteredAuthors = remember(authorQuery, authors) {
        if (authorQuery.isBlank()) authors
        else authors.filter { it.name.contains(authorQuery, ignoreCase = true) }
    }

    val grouped = remember(filteredAuthors) {
        groupByInitial(filteredAuthors) { it.name }
    }

    val letters = grouped.keys.sorted()
    val listState = rememberLazyListState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        OutlinedTextField(
            value = authorQuery,
            onValueChange = { authorQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search authors...") },
            singleLine = true
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = listState
        ) {
            letters.forEach { letter ->
                stickyHeader {
                    AlphabetHeader(letter)
                }

                items(grouped[letter]!!) { author ->
                    val isSelected = author.id in selectedAuthorIds

                    SelectableTagItem(
                        title = "${author.name} (${author.announcementCount})",
                        isSelected = isSelected,
                        onToggle = {
                            if (isSelected) selectedAuthorIds.remove(author.id)
                            else selectedAuthorIds.add(author.id)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Bottom apply button ALWAYS visible
        Button(
            onClick = {
                updateSelectedAuthorIds(selectedAuthorIds)
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Apply Authors")
        }
    }
}


@Composable
private fun SelectedChipsRow(
    selected: List<String>,
    onRemove: (String) -> Unit
) {
    if (selected.isEmpty()) return

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(selected) { text ->
            AssistChip(onClick = { onRemove(text) }, label = { Text(text) }, trailingIcon = {
                Icon(Icons.Default.Close, contentDescription = null)
            })
        }
    }
}

@Composable
private fun AlphabetHeader(char: Char) {
    Surface(
        tonalElevation = 2.dp, modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = char.toString(),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(8.dp)
        )
    }
}

private fun <T> groupByInitial(
    items: List<T>, titleProvider: (T) -> String
): Map<Char, List<T>> {
    return items.sortedBy { titleProvider(it).lowercase() }
        .groupBy { titleProvider(it).first().uppercaseChar() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewFullFilterSheetAuthor() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    FilterSheetAuthor(
        authors = previewAuthors,
        selectedIds = listOf(1, 3, 6),
        updateSelectedAuthorIds = {},
        sheetState = sheetState,
        onDismiss = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewHalfFilterSheetAuthor() {
    val sheetState = rememberModalBottomSheetState()
    FilterSheetAuthor(
        authors = previewAuthors,
        selectedIds = listOf(1, 3, 6),
        updateSelectedAuthorIds = {},
        sheetState = sheetState,
        onDismiss = {}
    )
}

val previewAuthors = listOf(
    Author(1, "A name", 2),
    Author(2, "B name", 0),
    Author(3, "B name", 150),
    Author(443, "B name", 345),
    Author(41, "B name", 343),
    Author(43, "B name", 3412),
    Author(4654, "B name", 345),
    Author(5, "C name", 600),
    Author(6, "D name", 1),
    Author(7, "E name", 456),
    Author(8, "Φ name", 34),
    Author(9, "Ω name", 254),
    Author(10, "Υ name", 264), Author(11, "Q name", 23645)

)