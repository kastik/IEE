package com.kastik.apps.feature.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.designsystem.utils.TrackScreenViewEvent
import com.kastik.apps.core.model.user.UserTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun SettingsRoute(
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    TrackScreenViewEvent("settings_screen")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {

        UiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
            ) {
                CircularWavyProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        is UiState.Success -> {
            SettingsScreenContent(
                theme = state.theme,
                setTheme = viewModel::setTheme,
                dynamicColorEnabled = state.isDynamicColorEnabled,
                setDynamicColor = viewModel::setDynamicColor,
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    theme: UserTheme,
    setTheme: (UserTheme) -> Unit = {},
    dynamicColorEnabled: Boolean,
    setDynamicColor: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
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
                                selected = theme, onSelected = setTheme
                            )
                        }
                        HorizontalDivider()
                        SettingSwitchRow(
                            title = "Dynamic color",
                            subtitle = "Use colors from the wallpaper",
                            checked = dynamicColorEnabled,
                            onCheckedChange = setDynamicColor
                        )
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
                            onCheckedChange = {
                                val text = "Not implemented yet!"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(context, text, duration) // in Activity
                                toast.show()

                            })
                        HorizontalDivider()
                        SettingSwitchRow(
                            title = "Email updates",
                            subtitle = "Send summaries to your inbox",
                            checked = false,
                            onCheckedChange = {
                                val text = "Not implemented yet!"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(context, text, duration) // in Activity
                                toast.show()

                            })
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
                            title = "About app", subtitle = "Version 1.0", onClick = {
                                val text = "Not implemented yet!"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(context, text, duration) // in Activity
                                toast.show()

                            })
                        HorizontalDivider()
                        SettingNavigationRow(
                            title = "Open source licenses", onClick = {
                                val text = "Not implemented yet!"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(context, text, duration) // in Activity
                                toast.show()

                            })
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
    selected: UserTheme, onSelected: (UserTheme) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        SegmentedButton(
            selected = selected == UserTheme.FOLLOW_SYSTEM,
            onClick = { onSelected(UserTheme.FOLLOW_SYSTEM) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
        ) {
            Text("System")
        }

        SegmentedButton(
            selected = selected == UserTheme.LIGHT,
            onClick = { onSelected(UserTheme.LIGHT) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
        ) {
            Text("Light")
        }

        SegmentedButton(
            selected = selected == UserTheme.DARK,
            onClick = { onSelected(UserTheme.DARK) },
            shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
        ) {
            Text("Dark")
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        theme = UserTheme.FOLLOW_SYSTEM,
        setTheme = {},
        dynamicColorEnabled = true,
        setDynamicColor = {})
}
