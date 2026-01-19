package com.kastik.apps.core.ui.sheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.text.Normalizer

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericFilterSheet(
    items: ImmutableList<T>,
    selectedIds: ImmutableList<Int>,
    onApply: (ImmutableList<Int>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    idProvider: (T) -> Int,
    labelProvider: (T) -> String,
    groupProvider: ((T) -> Char)? = null,
    titlePlaceholder: String = "Search..."
) {
    val analytics = LocalAnalytics.current
    var query by remember { mutableStateOf("") }
    val currentSelection =
        remember(selectedIds) { mutableStateListOf<Int>().apply { addAll(selectedIds) } }


    val filteredItems = remember(query, items) {
        if (query.isBlank()) items
        else items.filter {
            val normalizedQuery = query.removeAccents()
            labelProvider(it).removeAccents().contains(normalizedQuery, ignoreCase = true)
        }
    }

    // If a groupProvider is passed, we group. If null, we don't.
    val groupedItems = remember(filteredItems, groupProvider) {
        if (groupProvider != null) {
            filteredItems.sortedBy { labelProvider(it) }
                .groupBy { groupProvider(it) }
        } else emptyMap()
    }



    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = {
                analytics.logEvent("sheet_search", mapOf("query" to query))
                query = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .semantics {
                    contentDescription = "Some description"
                },
            placeholder = { Text(titlePlaceholder) },
            singleLine = true
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (groupProvider != null) {
                // GROUPED (Authors)
                groupedItems.keys.sorted().forEach { header ->
                    stickyHeader { AlphabetHeader(header) }
                    items(groupedItems[header] ?: emptyList()) { item ->
                        FilterRow(item, currentSelection, idProvider, labelProvider)
                    }
                }
            } else {
                // FLAT (Tags)
                items(filteredItems) { item ->
                    FilterRow(item, currentSelection, idProvider, labelProvider)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Bottom apply button ALWAYS visible
        Button(
            onClick = {
                onApply(currentSelection.toImmutableList())
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text("Apply Authors")
        }
    }
}

@Composable
private fun <T> FilterRow(
    item: T,
    selection: MutableList<Int>,
    idProvider: (T) -> Int,
    labelProvider: (T) -> String
) {
    val id = idProvider(item)
    val isSelected = id in selection

    SelectableTagItem(
        title = labelProvider(item),
        isSelected = isSelected,
        onToggle = {
            if (isSelected) selection.remove(id) else selection.add(id)
        }
    )
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

fun String.removeAccents(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{M}"), "")
}