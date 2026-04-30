package com.kastik.apps.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IEESheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchHint: String,
    hasSelection: Boolean,
    onClearSelection: () -> Unit,
    clearLabel: String,
    applyLabel: String,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(searchHint, overflow = TextOverflow.Ellipsis, maxLines = 1)
                },
                singleLine = true,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchQuery.isNotEmpty(), enter = scaleIn(), exit = scaleOut()
                    ) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = "Clear text"
                            )
                        }
                    }
                })

            AnimatedVisibility(
                visible = hasSelection,
                enter = scaleIn() + fadeIn() + expandHorizontally(),
                exit = scaleOut() + fadeOut() + shrinkHorizontally()
            ) {
                Button(
                    onClick = onClearSelection,
                    modifier = Modifier.wrapContentWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(clearLabel, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }

        content()

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                onApply()
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(applyLabel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun IEESheetPreview() {
    MaterialTheme {
        IEESheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            searchQuery = "Query",
            onSearchQueryChange = {},
            searchHint = "IDK",
            hasSelection = true,
            onClearSelection = {},
            clearLabel = "Clear",
            applyLabel = "Apply",
            onApply = {},
            onDismiss = {},
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f))
        }
    }
}