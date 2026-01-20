package com.kastik.apps.core.ui.topbar

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IEEFilterChip

@Composable
fun SearchBarFilters(
    selectedTagsCount: Int,
    selectedAuthorsCount: Int,
    openTagSheet: () -> Unit,
    openAuthorSheet: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        IEEFilterChip(
            label = "Tags",
            selectedCount = selectedTagsCount,
            onClick = openTagSheet

        )

        IEEFilterChip(
            label = "Authors",
            selectedCount = selectedAuthorsCount,
            onClick = openAuthorSheet
        )
    }

}