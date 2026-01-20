package com.kastik.apps.core.ui.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarCollapsed(
    searchBarState: SearchBarState,
    inputField: @Composable () -> Unit,
    scrollBehavior: SearchBarScrollBehavior,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    secondaryActions: @Composable () -> Unit = {},
) {
    Surface(
        modifier = modifier
            .then(with(scrollBehavior) { Modifier.searchBarScrollBehavior() })
    ) {
        Column {
            AppBarWithSearch(
                modifier = modifier,
                inputField = inputField,
                state = searchBarState,
                navigationIcon = navigationIcon,
                actions = actions,
            )
            Row { secondaryActions() }
        }
    }
}