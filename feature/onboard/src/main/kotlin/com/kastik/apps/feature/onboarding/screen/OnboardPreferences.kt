package com.kastik.apps.feature.onboarding.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.component.IeeRadioCard
import com.kastik.apps.core.designsystem.component.IeeSwitchCard
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Tailor your feed",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Customize how you want to discover announcements.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "Default Sorting",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.selectableGroup()) {
            IeeRadioCard(
                title = "Priority First",
                description = "Keep urgent and pinned announcements at the top.",
                selected = sortType == SortType.Priority,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSortTypeChange(SortType.Priority)
                })
            Spacer(modifier = Modifier.height(8.dp))
            IeeRadioCard(
                title = "Oldest First",
                description = "Read announcements chronologically as they were posted.",
                selected = sortType == SortType.ASC,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSortTypeChange(SortType.ASC)
                })
            Spacer(modifier = Modifier.height(8.dp))
            IeeRadioCard(
                title = "Newest First",
                description = "See the most recently published announcements at the top.",
                selected = sortType == SortType.DESC,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSortTypeChange(SortType.DESC)
                })
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "Feed Features",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        IeeSwitchCard(
            title = "Enable 'For You' Tab",
            subtitle = if (isForYouAvailable) "Curated feed based on specific tags and courses you subscribe to." else "Sign in required. Discover curated content based on your interests.",
            checked = isForYouEnabled,
            enabled = isForYouAvailable,
            onCheckedChange = { isEnabled ->
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onForYouChange(isEnabled)
            })

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "Search Behavior",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.selectableGroup()) {
            IeeRadioCard(
                title = "Titles Only",
                description = "Fastest search, matching only the headline of the announcement.",
                selected = searchScope == SearchScope.Title,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSearchScopeChange(SearchScope.Title)
                })
            Spacer(modifier = Modifier.height(8.dp))
            IeeRadioCard(
                title = "Body Only",
                description = "Deep search through the full content of the announcements.",
                selected = searchScope == SearchScope.Body,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSearchScopeChange(SearchScope.Body)
                })
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onFinishClick()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Continue")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun OnboardPreferencesPreview() {
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