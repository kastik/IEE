package com.kastik.apps.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    SettingsScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings", style = MaterialTheme.typography.titleLarge
                    )
                })

        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text("Appearance", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(size = 20.dp)
                ) {
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Text("Theme", style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(8.dp))
                            ThemeSegmentedButton(
                                selected = ThemeMode.SYSTEM, onSelected = { })
                        }
                        Divider()
                        SettingSwitchRow(
                            title = "Dynamic color",
                            subtitle = "Use colors from the wallpaper",
                            checked = true,
                            onCheckedChange = { })
                    }
                }
            }

            item {
                Text("Notifications", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)
                ) {
                    Column {
                        SettingSwitchRow(
                            title = "Push notifications",
                            subtitle = "Receive updates and announcements",
                            checked = false,
                            onCheckedChange = { })
                        Divider()
                        SettingSwitchRow(
                            title = "Email updates",
                            subtitle = "Send summaries to your inbox",
                            checked = true,
                            onCheckedChange = { })
                    }
                }
            }

            item {
                Text("About", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)
                ) {
                    Column {
                        SettingNavigationRow(
                            title = "About app",
                            subtitle = "Version 1.0",
                            onClick = { /* navigate */ })
                        Divider()
                        SettingNavigationRow(
                            title = "Open source licenses", onClick = { /* navigate */ })
                    }
                }
            }
        }
    }

}

@Composable
private fun SettingSwitchRow(
    title: String, subtitle: String? = null, checked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked, onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingNavigationRow(
    title: String, subtitle: String? = null, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSegmentedButton(
    selected: ThemeMode, onSelected: (ThemeMode) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        SegmentedButton(
            selected = selected == ThemeMode.SYSTEM,
            onClick = { onSelected(ThemeMode.SYSTEM) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
        ) {
            Text("System")
        }

        SegmentedButton(
            selected = selected == ThemeMode.LIGHT,
            onClick = { onSelected(ThemeMode.LIGHT) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
        ) {
            Text("Light")
        }

        SegmentedButton(
            selected = selected == ThemeMode.DARK,
            onClick = { onSelected(ThemeMode.DARK) },
            shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
        ) {
            Text("Dark")
        }
    }
}

enum class ThemeMode { SYSTEM, LIGHT, DARK }

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent()
}
