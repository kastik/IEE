package com.kastik.apps.feature.settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kastik.apps.core.common.extensions.launchUrl
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserTheme
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.placeholder.LoadingContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun SettingsRoute(
    navigateToLicenses: () -> Unit,
    viewModel: SettingsScreenViewModel = hiltViewModel(),
) {
    TrackScreenViewEvent("settings_screen")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState, contentKey = { state ->
            state::class
        }) { state ->
        when (state) {

            is UiState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())

            is UiState.Success -> {
                SettingsScreenContent(
                    theme = state.theme,
                    onThemeChange = viewModel::setTheme,
                    sortType = state.sortType,
                    onSortTypeChange = viewModel::setSortType,
                    searchScope = state.searchScope,
                    onSearchScopeChange = viewModel::setSearchScope,
                    dynamicColor = state.isDynamicColorEnabled,
                    onDynamicColorChange = viewModel::setDynamicColor,
                    forYouEnabled = state.isForYouEnabled,
                    onForYouChange = viewModel::setEnableForYou,
                    fabFiltersDisabled = state.areFabFiltersEnabled,
                    onFabFiltersChange = viewModel::setFabFilters,
                    navigateToLicenses = navigateToLicenses
                )
            }

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun SettingsScreenContent(
    theme: UserTheme,
    onThemeChange: (UserTheme) -> Unit = {},
    sortType: SortType,
    onSortTypeChange: (SortType) -> Unit = {},
    searchScope: SearchScope,
    onSearchScopeChange: (SearchScope) -> Unit = {},
    dynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit = {},
    forYouEnabled: Boolean,
    onForYouChange: (Boolean) -> Unit = {},
    fabFiltersDisabled: Boolean,
    onFabFiltersChange: (Boolean) -> Unit = {},
    navigateToLicenses: () -> Unit = {}
) {

    val context = LocalContext.current
    val analytics = LocalAnalytics.current
    val hapticFeedback = LocalHapticFeedback.current

    val areNotificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            Manifest.permission.POST_NOTIFICATIONS
        ).status.isGranted
    } else {
        true
    }


    val versionName = remember {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName
        } catch (e: Exception) {
            "Unknown"
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "settings:content" },
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Text(
                text = "Feed Options",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = "Sort by",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        SettingsegmentedButton(
                            selected = sortType,
                            options = SortType.entries.toImmutableList(),
                            label = {
                                when (it) {
                                    SortType.Priority -> "Priority"
                                    SortType.DESC -> "Descending"
                                    SortType.ASC -> "Ascending"
                                }
                            },
                            onSelected = { sortType ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                onSortTypeChange(sortType)
                                analytics.logEvent(
                                    "sort_type_changed", mapOf("sort_type" to sortType.name)
                                )
                            })
                    }
                    HorizontalDivider()
                    SettingSwitchRow(
                        title = "For You",
                        subtitle = "Show announcements from subscribed tags",
                        checked = forYouEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                            } else {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                            }
                            onForYouChange(enabled)
                            analytics.logEvent(
                                "for_you_changed",
                                mapOf("for_you_enabled" to enabled.toString())
                            )
                        })
                    HorizontalDivider()
                    SettingSwitchRow(
                        title = "FAB filters",
                        subtitle = "Show filters in the FAB button",
                        checked = fabFiltersDisabled,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                            } else {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                            }
                            onFabFiltersChange(enabled)
                            analytics.logEvent(
                                "fab_filters_changed",
                                mapOf("fab_filters_enabled" to enabled.toString())
                            )
                        })
                }
            }

            Text(
                text = "Search Options",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(size = 20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

            ) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = "Search in",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        SettingsegmentedButton(
                            selected = searchScope,
                            options = SearchScope.entries.toImmutableList(),
                            label = {
                                when (it) {
                                    SearchScope.Title -> "Title"
                                    SearchScope.Body -> "Body"
                                    SearchScope.TitleAndBody -> "Both"
                                }
                            },
                            onSelected = { searchScope ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                onSearchScopeChange(searchScope)
                                analytics.logEvent(
                                    "search_scope_changed", mapOf("search_type" to searchScope.name)
                                )
                            })
                    }
                }
            }



            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(size = 20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

            ) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = "Theme",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        SettingsegmentedButton(
                            selected = theme,
                            options = UserTheme.entries.toImmutableList(),
                            label = {
                                when (it) {
                                    UserTheme.FOLLOW_SYSTEM -> "System"
                                    UserTheme.LIGHT -> "Light"
                                    UserTheme.DARK -> "Dark"
                                }
                            },
                            onSelected = { theme ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                onThemeChange(theme)
                                analytics.logEvent(
                                    "theme_changed", mapOf("theme" to theme.name)
                                )
                            })
                    }
                    HorizontalDivider()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        SettingSwitchRow(
                            title = "Dynamic color",
                            subtitle = "Use colors from the wallpaper",
                            checked = dynamicColor,
                            onCheckedChange = { enabled ->
                                if (enabled) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                } else {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                                }
                                onDynamicColorChange(enabled)
                                analytics.logEvent(
                                    "dynamic_color_changed",
                                    mapOf("dynamic_color_enabled" to enabled.toString())
                                )
                            })
                    }
                }
            }

            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

            ) {
                Column {
                    SettingSwitchRow(
                        title = "Push notifications",
                        subtitle = "Receive updates and announcements",
                        checked = areNotificationGranted,
                        onCheckedChange = {
                            analytics.logEvent(
                                "push_notifications_changed",
                                mapOf("push_notifications_enabled" to it.toString())
                            )
                            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                }
                            } else {
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            }
                            context.startActivity(intent)
                        })
                }
            }

            Text(
                text = "About",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

            ) {
                Column {
                    SettingNavigationRow(
                        title = "About app", subtitle = "Version $versionName", onClick = {
                            context.launchUrl("https://github.com/kastik/IEE")
                        })
                    HorizontalDivider()
                    SettingNavigationRow(
                        title = "Open source licenses", onClick = {
                            analytics.logEvent("open_source_licenses_clicked")
                            navigateToLicenses()
                        })
                }
            }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SettingsegmentedButton(
    selected: T,
    options: ImmutableList<T>,
    label: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selected == option,
                onClick = { onSelected(option) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
            ) {
                Text(
                    text = label(option),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected == option) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = null
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null)
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        theme = UserTheme.FOLLOW_SYSTEM,
        onThemeChange = {},
        dynamicColor = true,
        onDynamicColorChange = {},
        sortType = SortType.Priority,
        onSortTypeChange = {},
        searchScope = SearchScope.Title,
        onSearchScopeChange = {},
        forYouEnabled = false,
        onForYouChange = {},
        fabFiltersDisabled = false,
        onFabFiltersChange = {},
        navigateToLicenses = {},
    )
}
