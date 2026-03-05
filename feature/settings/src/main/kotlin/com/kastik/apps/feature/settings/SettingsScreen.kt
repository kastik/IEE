package com.kastik.apps.feature.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kastik.apps.core.common.extensions.launchUrl
import com.kastik.apps.core.designsystem.component.IEEIconToolTip
import com.kastik.apps.core.designsystem.component.IEESliderThumbToolTip
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserTheme
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.placeholder.LoadingContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.roundToInt

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
                    isForYouAvailable = state.isForYouAvailable,
                    onForYouChange = viewModel::setEnableForYou,
                    fabFiltersDisabled = state.areFabFiltersEnabled,
                    onFabFiltersChange = viewModel::setFabFilters,
                    announcementCheckIntervalHours = state.announcementCheckIntervalHours,
                    isAnnouncementCheckIntervalAvailable = state.isAnnouncementCheckIntervalAvailable,
                    setAnnouncementCheckIntervalHours = viewModel::setAnnouncementCheckIntervalHours,
                    areNotificationsAllowed = state.areNotificationsAllowed,
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
    isForYouAvailable: Boolean,
    onForYouChange: (Boolean) -> Unit = {},
    fabFiltersDisabled: Boolean,
    onFabFiltersChange: (Boolean) -> Unit = {},
    announcementCheckIntervalHours: Int,
    isAnnouncementCheckIntervalAvailable: Boolean,
    setAnnouncementCheckIntervalHours: (Int) -> Unit = {},
    areNotificationsAllowed: Boolean,
    navigateToLicenses: () -> Unit = {},
) {

    val context = LocalContext.current
    val analytics = LocalAnalytics.current
    val hapticFeedback = LocalHapticFeedback.current


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
                text = stringResource(R.string.title_feed_options),
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
                            text = stringResource(R.string.sort_by_label),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        SettingsegmentedButton(
                            selected = sortType,
                            options = SortType.entries.toImmutableList(),
                            label = {
                                when (it) {
                                    SortType.Priority -> stringResource(R.string.sort_by_priority_label)
                                    SortType.DESC -> stringResource(R.string.sort_by_descending_label)
                                    SortType.ASC -> stringResource(R.string.sort_by_ascending_label)
                                }
                            },
                            onSelected = { sortType ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                onSortTypeChange(sortType)
                                analytics.setUserProperty("sort_type", sortType.name)
                                analytics.logEvent(
                                    "sort_type_changed",
                                    mapOf(
                                        "sort_type" to sortType.name,
                                        "source" to "settings_screen"
                                    )
                                )
                            })
                    }
                    HorizontalDivider()
                    SettingSwitchRow(
                        title = stringResource(R.string.for_you_label),
                        subtitle = if (isForYouAvailable) stringResource(R.string.for_you_available_description) else stringResource(
                            R.string.for_unavailable_description
                        ),
                        enabled = isForYouAvailable,
                        checked = forYouEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                            } else {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                            }
                            onForYouChange(enabled)
                            analytics.setUserProperty("for_you", enabled.toString())
                            analytics.logEvent(
                                "for_you_changed", mapOf(
                                    "for_you" to enabled.toString(),
                                    "source" to "settings_screen"
                                )
                            )
                        })
                    HorizontalDivider()
                    SettingSwitchRow(
                        title = stringResource(R.string.fab_filters_label),
                        subtitle = stringResource(R.string.fab_filters_description),
                        checked = fabFiltersDisabled,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                            } else {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                            }
                            onFabFiltersChange(enabled)
                            analytics.setUserProperty("fab_filters", enabled.toString())
                            analytics.logEvent(
                                "fab_filters_changed", mapOf(
                                    "fab_filters" to enabled.toString(),
                                    "source" to "settings_screen"
                                )
                            )
                        })
                }
            }

            Text(
                text = stringResource(R.string.title_search_options),
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
                            text = stringResource(R.string.search_in_label),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        SettingsegmentedButton(
                            selected = searchScope,
                            options = SearchScope.entries.toImmutableList(),
                            label = {
                                when (it) {
                                    SearchScope.Title -> stringResource(R.string.search_in_title_label)
                                    SearchScope.Body -> stringResource(R.string.search_in_body_label)
                                    SearchScope.TitleAndBody -> stringResource(R.string.search_in_both_label)
                                }
                            },
                            onSelected = { searchScope ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                onSearchScopeChange(searchScope)
                                analytics.setUserProperty("search_scope", searchScope.name)
                                analytics.logEvent(
                                    "search_scope_changed", mapOf(
                                        "search_scope" to searchScope.name,
                                        "source" to "settings_screen"
                                    )
                                )
                            })
                    }
                }
            }



            Text(
                text = stringResource(R.string.title_appearance),
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
                            text = stringResource(R.string.theme_label),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        SettingsegmentedButton(
                            selected = theme,
                            options = UserTheme.entries.toImmutableList(),
                            label = {
                                when (it) {
                                    UserTheme.FOLLOW_SYSTEM -> stringResource(R.string.theme_system_label)
                                    UserTheme.LIGHT -> stringResource(R.string.theme_light_label)
                                    UserTheme.DARK -> stringResource(R.string.theme_dark_label)
                                }
                            },
                            onSelected = { theme ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                onThemeChange(theme)
                                analytics.setUserProperty("theme", theme.name)
                                analytics.logEvent(
                                    "theme_changed", mapOf(
                                        "theme" to theme.name,
                                        "source" to "settings_screen"
                                    )
                                )
                            })
                    }
                    HorizontalDivider()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        SettingSwitchRow(
                            title = stringResource(R.string.dynamic_color_label),
                            subtitle = stringResource(R.string.dynamic_color_description),
                            checked = dynamicColor,
                            onCheckedChange = { enabled ->
                                if (enabled) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                } else {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                                }
                                onDynamicColorChange(enabled)
                                analytics.setUserProperty("dynamic_color", enabled.toString())
                                analytics.logEvent(
                                    "dynamic_color_changed", mapOf(
                                        "dynamic_color" to enabled.toString(),
                                        "source" to "settings_screen"
                                    )
                                )
                            })
                    }
                }
            }

            Text(
                text = stringResource(R.string.title_notifications),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

            ) {
                Column {
                    SettingSwitchRow(
                        title = stringResource(R.string.push_notifications_label),
                        subtitle = stringResource(R.string.push_notifications_description),
                        checked = areNotificationsAllowed,
                        onCheckedChange = {
                            analytics.setUserProperty("push_notifications", it.toString())
                            analytics.logEvent(
                                "push_notifications_changed", mapOf(
                                    "push_notifications" to it.toString(),
                                    "source" to "settings_screen"
                                )
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
                    HorizontalDivider()
                    SettingsSliderRow(
                        title = stringResource(R.string.announcement_check_interval_label),
                        steps = 22,
                        initialValue = announcementCheckIntervalHours.toFloat(),
                        valueRange = 1f..24f,
                        enabled = isAnnouncementCheckIntervalAvailable,
                        tooltipTitle = stringResource(R.string.announcement_check_interval_warning_title),
                        tooltipBody = stringResource(R.string.announcement_check_interval_warning_body),
                        valueFormatter = { formatIntervalToHours(it) },
                        onValueChangeFinished = {
                            setAnnouncementCheckIntervalHours(it)
                            analytics.setUserProperty(
                                "announcement_interval",
                                announcementCheckIntervalHours.toString()
                            )
                            analytics.logEvent(
                                "announcement_interval_changed", mapOf(
                                    "announcement_interval" to announcementCheckIntervalHours.toString(),
                                    "source" to "settings_screen"
                                )
                            )
                        }
                    )
                }
            }

            Text(
                text = stringResource(R.string.title_about),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

            ) {
                Column {
                    SettingNavigationRow(
                        title = stringResource(R.string.about_app_label),
                        subtitle = "${stringResource(R.string.about_app_description)} $versionName",
                        onClick = {
                            analytics.logEvent(
                                "about_app_clicked",
                                mapOf("source" to "settings_screen")
                            )
                            context.launchUrl("https://github.com/kastik/IEE")
                        })
                    HorizontalDivider()
                    SettingNavigationRow(
                        title = stringResource(R.string.open_source_label), onClick = {
                            analytics.logEvent(
                                "open_source_licenses_clicked",
                                mapOf("source" to "settings_screen")
                            )
                            navigateToLicenses()
                        })
                    HorizontalDivider()
                    SettingNavigationRow(
                        title = stringResource(R.string.discord_label), onClick = {
                            analytics.logEvent(
                                "discord_channel_clicked",
                                mapOf("source" to "settings_screen")
                            )
                            context.launchUrl("https://discord.com/channels/693584494862794822/1473065482058993765")
                        })
                }
            }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> SettingsegmentedButton(
    selected: T,
    options: ImmutableList<T>,
    label: @Composable (T) -> String,
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
private fun formatIntervalToHours(hours: Int): String {
    val hourLabel = pluralStringResource(R.plurals.announcement_check_interval_hours, hours)
    return "$hours $hourLabel"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSliderRow(
    title: String,
    steps: Int,
    initialValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    enabled: Boolean,
    tooltipTitle: String,
    tooltipBody: String,
    valueFormatter: @Composable (Int) -> String,
    onValueChangeFinished: (Int) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var sliderValue by remember { mutableFloatStateOf(initialValue) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
            IEEIconToolTip(
                tooltipTitle = {
                    Text(
                        text = tooltipTitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                tooltipBody = {
                    Text(
                        text = tooltipBody,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null
                    )
                }
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                enabled = enabled,
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = valueRange,
                steps = steps,
                interactionSource = interactionSource,
                onValueChangeFinished = {
                    onValueChangeFinished(sliderValue.roundToInt())
                },
                thumb = {
                    IEESliderThumbToolTip(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        tooltipText = valueFormatter(sliderValue.roundToInt())
                    )
                },
            )
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String? = null,
    enabled: Boolean = true,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .toggleable(
                enabled = enabled,
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
            enabled = enabled,
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
        isForYouAvailable = true,
        onForYouChange = {},
        fabFiltersDisabled = false,
        onFabFiltersChange = {},
        announcementCheckIntervalHours = 1,
        isAnnouncementCheckIntervalAvailable = true,
        setAnnouncementCheckIntervalHours = {},
        areNotificationsAllowed = true,
        navigateToLicenses = {},
    )
}

