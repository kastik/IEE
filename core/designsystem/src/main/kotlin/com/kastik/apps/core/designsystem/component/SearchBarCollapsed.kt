package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.windowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarCollapsed(
    modifier: Modifier = Modifier,
    scrollBehavior: SearchBarScrollBehavior,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    isSignedIn: Boolean,
    onSignInClick: () -> Unit,
    navigateToSearch: () -> Unit
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .windowInsetsPadding(windowInsets)
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            IconButton(
                onClick = { if (isSignedIn) navigateToProfile() else onSignInClick() }) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                onClick = navigateToSearch,
                shape = SearchBarDefaults.inputFieldShape,
                tonalElevation = 32.dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
                    .sizeIn(minHeight = SearchBarDefaults.InputFieldHeight)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )

                    Text(
                        "Search…",
                        modifier = Modifier.padding(start = 12.dp),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }
            IconButton(onClick = navigateToSettings) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
fun PreviewSearchBarCollapsed() {
    SearchBarCollapsed(
        scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior(),
        navigateToSettings = {},
        navigateToProfile = { },
        isSignedIn = false,
        onSignInClick = {},
        navigateToSearch = {},
    )
}