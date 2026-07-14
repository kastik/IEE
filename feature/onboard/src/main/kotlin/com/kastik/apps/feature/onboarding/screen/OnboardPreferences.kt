package com.kastik.apps.feature.onboarding.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.component.IeeRadioCard
import com.kastik.apps.core.designsystem.component.IeeSwitchCard
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.feature.onboarding.R

@Composable
internal fun OnboardPreferences(
    isForYouAvailable: Boolean,
    isForYouEnabled: Boolean,
    sortType: SortType,
    searchScope: SearchScope,
    onForYouChange: (Boolean) -> Unit,
    onSortTypeChange: (SortType) -> Unit,
    onSearchScopeChange: (SearchScope) -> Unit,
    onFinishClick: () -> Unit
) {

    val haptics = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        OnboardPreferencesTitle()


        OnboardPreferencesSorting(
            sortType = sortType,
            onSortTypeChange = onSortTypeChange
        )

        OnboardPreferencesFeedFeatures(
            isForYouAvailable = isForYouAvailable,
            isForYouEnabled = isForYouEnabled,
            onForYouChange = onForYouChange
        )


        OnboardPreferencesSearchBehaviour(
            searchScope = searchScope,
            onSearchScopeChange = onSearchScopeChange
        )


        Button(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onFinishClick()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(stringResource(R.string.preferences_next_page))
        }
    }
}


@Composable
fun OnboardPreferencesTitle() {
    Column() {
        Text(
            text = stringResource(R.string.preferences_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = stringResource(R.string.preferences_body),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun OnboardPreferencesSorting(
    sortType: SortType,
    onSortTypeChange: (SortType) -> Unit,
) {

    val haptics = LocalHapticFeedback.current

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            stringResource(R.string.preferences_sorting_title),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IeeRadioCard(
                title = stringResource(R.string.preferences_sorting_priority_title),
                description = stringResource(R.string.preferences_sorting_priority_body),
                selected = sortType == SortType.Priority,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSortTypeChange(SortType.Priority)
                })
            IeeRadioCard(
                title = stringResource(R.string.preferences_sorting_oldest_title),
                description = stringResource(R.string.preferences_sorting_oldest_body),
                selected = sortType == SortType.ASC,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSortTypeChange(SortType.ASC)
                })
            IeeRadioCard(
                title = stringResource(R.string.preferences_sorting_newest_title),
                description = stringResource(R.string.preferences_sorting_newest_body),
                selected = sortType == SortType.DESC,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSortTypeChange(SortType.DESC)
                })
        }
    }
}


@Composable
private fun OnboardPreferencesFeedFeatures(
    isForYouAvailable: Boolean,
    isForYouEnabled: Boolean,
    onForYouChange: (Boolean) -> Unit,
) {

    val haptics = LocalHapticFeedback.current

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Text(
            stringResource(R.string.preferences_feed_title),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        IeeSwitchCard(
            title = stringResource(R.string.preferences_feed_foryou_title),
            subtitle = stringResource(if (isForYouAvailable) R.string.preferences_feed_foryou_body_available else R.string.preferences_feed_foryou_body_unavailable),
            checked = isForYouEnabled,
            enabled = isForYouAvailable,
            onCheckedChange = { isEnabled ->
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onForYouChange(isEnabled)
            })

    }
}


@Composable
private fun OnboardPreferencesSearchBehaviour(
    searchScope: SearchScope,
    onSearchScopeChange: (SearchScope) -> Unit,
) {

    val haptics = LocalHapticFeedback.current

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            stringResource(R.string.preferences_search_title),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IeeRadioCard(
                title = stringResource(R.string.preferences_search_titles_title),
                description = stringResource(R.string.preferences_search_titles_body),
                selected = searchScope == SearchScope.Title,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSearchScopeChange(SearchScope.Title)
                })
            IeeRadioCard(
                title = stringResource(R.string.preferences_search_body_title),
                description = stringResource(R.string.preferences_search_body_body),
                selected = searchScope == SearchScope.Body,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSearchScopeChange(SearchScope.Body)
                })
        }
    }
}


@Preview
@Composable
private fun OnboardPreferencesPreview() {
    IeePreview {
        OnboardPreferences(
            isForYouAvailable = false,
            isForYouEnabled = true,
            sortType = SortType.ASC,
            searchScope = SearchScope.Body,
            onForYouChange = { },
            onSortTypeChange = { },
            onSearchScopeChange = { },
            onFinishClick = { }
        )
    }
}