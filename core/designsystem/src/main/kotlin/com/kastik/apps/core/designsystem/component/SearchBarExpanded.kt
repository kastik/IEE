package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults.windowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarExpanded(
    scrollBehavior: SearchBarScrollBehavior,
    modifier: Modifier = Modifier,
    query: String,
    updateQuery: (String) -> Unit,
    navigateBack: () -> Unit,
    toggleTagSheet: () -> Unit,
    toggleAuthorSheet: () -> Unit,
    selectedTagCount: Int,
    selectedAuthorCount: Int
) {


    Surface(
        tonalElevation = 6.dp,
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .then(
                with(scrollBehavior) { Modifier.searchBarScrollBehavior() })
            .fillMaxWidth()
            .semantics { isTraversalGroup = true },
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .windowInsetsPadding(windowInsets)
                .padding(12.dp)
        ) {
            Surface(
                shape = SearchBarDefaults.inputFieldShape,
                tonalElevation = 32.dp,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .sizeIn(minHeight = SearchBarDefaults.InputFieldHeight)
            ) {
                TextField(
                    value = query, onValueChange = updateQuery, singleLine = true, placeholder = {
                        Text(
                            text = "Search...", style = MaterialTheme.typography.bodyMedium
                        )
                    }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            ) {
                ElevatedFilterChip(selected = false, onClick = { toggleTagSheet() }, label = {
                    Text(
                        text = if (selectedTagCount > 0) "Tags [$selectedTagCount]" else "Tags",
                        style = MaterialTheme.typography.titleSmall
                    )
                }, trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) })
                ElevatedFilterChip(selected = false, onClick = {
                    toggleAuthorSheet()
                }, label = {
                    Text(
                        text = if (selectedAuthorCount > 0) "Authors [$selectedAuthorCount]" else "Authors",
                        style = MaterialTheme.typography.titleSmall

                    )
                }, trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) })
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewSearchBarExpanded() {
    SearchBarExpanded(
        scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior(),
        query = "",
        updateQuery = {},
        navigateBack = {},
        toggleTagSheet = {},
        toggleAuthorSheet = {},
        selectedTagCount = 6,
        selectedAuthorCount = 0
    )
}