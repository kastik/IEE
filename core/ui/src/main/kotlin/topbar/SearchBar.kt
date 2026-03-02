package com.kastik.apps.core.ui.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.kastik.apps.core.model.search.QuickResults
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    quickResults: QuickResults,
    onTagQuickResultClick: (tagId: Int) -> Unit,
    onAuthorQuickResultClick: (authorId: Int) -> Unit,
    onAnnouncementQuickResultClick: (announcementId: Int) -> Unit,
    onSearch: (query: String) -> Unit,
    searchHint: String,
    textFieldState: TextFieldState,
    searchBarState: SearchBarState,
    scrollBehavior: SearchBarScrollBehavior,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedSecondaryActions: @Composable () -> Unit = {},
    collapsedSecondaryActions: @Composable () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(searchBarState.isAnimating) {
        if (searchBarState.targetValue == SearchBarValue.Collapsed) {
            focusManager.clearFocus()
        }
    }


    val searchBarInputField = remember {
        movableContentOf {
            SearchBarInputField(
                placeholder = searchHint,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                modifier = Modifier.semantics { contentDescription = "search_bar:input_field" },
                onSearch = { query ->
                    scope.launch {
                        searchBarState.animateToCollapsed()
                        onSearch(query)
                    }
                },
            )
        }
    }

    SearchBarCollapsed(
        modifier = modifier,
        actions = actions,
        inputField = searchBarInputField,
        scrollBehavior = scrollBehavior,
        searchBarState = searchBarState,
        navigationIcon = navigationIcon,
        collapsedSecondaryActions = collapsedSecondaryActions,
    )

    if (searchBarState.currentValue == SearchBarValue.Expanded) {
        SearchBarExpanded(
            modifier = modifier,
            quickResults = quickResults,
            searchBarState = searchBarState,
            inputField = searchBarInputField,
            expandedSecondaryActions = expandedSecondaryActions,
            onTagQuickResultClick = { tagId ->
                scope.launch {
                    searchBarState.animateToCollapsed()
                    onTagQuickResultClick(tagId)
                }
            },
            onAuthorQuickResultClick = { authorId ->
                scope.launch {
                    searchBarState.animateToCollapsed()
                    onAuthorQuickResultClick(authorId)
                }
            },
            onAnnouncementQuickResultClick = {
                scope.launch {
                    searchBarState.animateToCollapsed()
                    onAnnouncementQuickResultClick(it)
                }
            },

            )
    }
}