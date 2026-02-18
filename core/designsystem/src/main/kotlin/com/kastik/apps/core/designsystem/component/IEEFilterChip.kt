package com.kastik.apps.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun IEEFilterChip(
    label: String,
    selectedCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    val displayLabel = if (selectedCount > 0) "$label ($selectedCount)" else label

    ElevatedFilterChip(
        modifier = modifier,
        leadingIcon = icon?.let { { Icon(it, null) } },
        selected = selectedCount > 0,
        onClick = onClick,
        label = {
            Text(
                text = displayLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
    )
}

@Preview(name = "IEEFilterChip")
@Composable
fun PreviewIEEFilterChip() {
    IEEFilterChip(
        label = "Authors",
        selectedCount = 0,
        onClick = {}
    )
}

@Preview(name = "IEEFilterChip with icon")
@Composable
fun PreviewIEEFilterChipWithIcon() {
    IEEFilterChip(
        icon = Icons.Default.Person,
        label = "Authors",
        selectedCount = 0,
        onClick = {}
    )
}

@Preview(name = "IEEFilterChip with selection")
@Composable
fun PreviewIEEFilterChipWithSelection() {
    IEEFilterChip(
        label = "Authors",
        selectedCount = 4,
        onClick = {}
    )
}

@Preview(name = "IEEFilterChip with selection and icon")
@Composable
fun PreviewIEEFilterChipWithSelectionAndIcon() {
    IEEFilterChip(
        icon = Icons.Default.Person,
        label = "Authors",
        selectedCount = 4,
        onClick = {}
    )
}