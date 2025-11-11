package com.kastik.home.components

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TestNewSearchBar(
    scrollBehavior: SearchBarScrollBehavior,
    searchBarState: SearchBarState,
    textFieldState: TextFieldState,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    isSignedIn: Boolean
) {
    val context = LocalContext.current
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
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        ("https://login.it.teithe.gr/authorization?" + "client_id=690a9861468c9b767cabdc40" + "&response_type=code" + "&scope=announcements,profile" + "&redirect_uri=com.kastik.apps://auth").toUri()
                    )
                    context.startActivity(intent)
                }
            }) {
                Icon(Icons.Default.AccountCircle, null)
            }
        }
    )
}