package com.kastik.apps.core.ui.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.common.extensions.removeAccents
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericFilterSheet(
    modifier: Modifier = Modifier,
    items: ImmutableList<T>,
    selectedIds: ImmutableList<Int>,
    onApply: (ImmutableList<Int>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    idProvider: (T) -> Int,
    labelProvider: (T) -> String,
    groupProvider: ((T) -> Char)? = null,
    titlePlaceholder: String = "Search...",
    applyText: String = "Apply"
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

    val groupedItems = remember(filteredItems, groupProvider) {
        if (groupProvider != null) {
            filteredItems.sortedBy { labelProvider(it) }
                .groupBy { groupProvider(it) }
        } else emptyMap()
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    analytics.logEvent("sheet_search", mapOf("query" to query))
                    query = it
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text(titlePlaceholder) },
                singleLine = true,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = query.isNotEmpty(),
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        IconButton(onClick = { query = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear text"
                            )
                        }
                    }
                }
            )
            AnimatedVisibility(
                visible = currentSelection.isNotEmpty(),
                enter = scaleIn() + fadeIn() + expandHorizontally(),
                exit = scaleOut() + fadeOut() + shrinkHorizontally()
            ) {
                Button(
                    onClick = { currentSelection.clear() },
                    modifier = Modifier.wrapContentWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Clear", maxLines = 1)
                }
            }
        }


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (groupProvider != null) {
                groupedItems.keys.sorted().forEach { header ->
                    stickyHeader { AlphabetHeader(header) }
                    items(groupedItems[header] ?: emptyList()) { item ->
                        FilterRow(item, currentSelection, idProvider, labelProvider)
                    }
                }
            } else {
                items(filteredItems) { item ->
                    FilterRow(item, currentSelection, idProvider, labelProvider)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                onApply(currentSelection.toImmutableList())
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(applyText)
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

