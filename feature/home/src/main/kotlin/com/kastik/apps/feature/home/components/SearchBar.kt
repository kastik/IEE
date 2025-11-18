package com.kastik.apps.feature.home.components

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TestNewSearchBar(
    scrollBehavior: SearchBarScrollBehavior,
    searchBarState: SearchBarState,
    textFieldState: TextFieldState,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    isSignedIn: Boolean,
    onSignInClick: () -> Unit,
) {
    AppBarWithSearch(
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        inputField = {
            SearchBarDefaults.InputField(
                textFieldState = textFieldState,
                searchBarState = searchBarState,
                onSearch = {},
                placeholder = { Text("Search...", textAlign = TextAlign.Center) })
        },
        actions = {
            IconButton(onClick = navigateToSettings) {
                Icon(Icons.Default.Settings, null)
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                if (isSignedIn) {
                    navigateToProfile()
                } else {
                    onSignInClick()
                }
            }) {
                Icon(Icons.Default.AccountCircle, null)
            }
        }
    )
}