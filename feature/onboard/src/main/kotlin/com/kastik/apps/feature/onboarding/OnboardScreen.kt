package com.kastik.apps.feature.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.common.extensions.launchSignIn
import com.kastik.apps.core.designsystem.extensions.TrackScreenViewEvent
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.onboard.OnboardStage
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme
import com.kastik.apps.core.ui.extensions.predictiveBackPagerEffect
import com.kastik.apps.core.ui.paging.rememberPagerPredictiveBackState
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.feature.onboarding.screen.OnboardAppearance
import com.kastik.apps.feature.onboarding.screen.OnboardFinish
import com.kastik.apps.feature.onboarding.screen.OnboardNotifications
import com.kastik.apps.feature.onboarding.screen.OnboardPreferences
import com.kastik.apps.feature.onboarding.screen.OnboardSignIn
import com.kastik.apps.feature.onboarding.screen.OnboardWelcome
import kotlinx.coroutines.launch

@Composable
internal fun OnboardRoute(
    viewModel: OnboardViewModel = hiltViewModel(),
    onFinish: () -> Unit = {},
) {
    TrackScreenViewEvent(screenClass = "onboarding_route", screenName = "onboarding_screen")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState,
        contentKey = { it::class }
    ) { state ->

        when (state) {
            OnboardUiState.Loading -> {
                OnboardLoading()
            }

            is OnboardUiState.Success -> {
                OnboardSuccess(
                    currentOnboardStage = state.onboardStage,
                    onStageChange = viewModel::onStageChange,
                    isSignedIn = state.isSignedIn,
                    onSkipSignIn = viewModel::onSkipSignIn,
                    isForYouEnabled = state.isForYouEnabled,
                    isForYouAvailable = state.isForYouAvailable,
                    onForYouChange = viewModel::onForYouChange,
                    sortType = state.sortType,
                    onSortTypeChange = viewModel::onSortTypeChange,
                    searchScope = state.searchScope,
                    onSearchScopeChange = viewModel::onSearchScopeChange,
                    areNotificationsAllowed = state.areNotificationsAllowed,
                    theme = state.theme,
                    onThemeChange = viewModel::onThemeChange,
                    isDynamicColorEnabled = state.isDynamicColorEnabled,
                    onDynamicColorChange = viewModel::onDynamicColorChange,
                    onFinishOnboarding = {
                        viewModel.onFinishedOnboarding()
                        onFinish()
                    }
                )
            }
        }
    }


}

@Composable
fun OnboardLoading() {
    LoadingContent(
        modifier = Modifier.fillMaxSize(),
    )
}


@Composable
fun OnboardSuccess(
    currentOnboardStage: OnboardStage,
    onStageChange: (OnboardStage) -> Unit,
    isSignedIn: Boolean,
    onSkipSignIn: () -> Unit,
    isForYouEnabled: Boolean,
    isForYouAvailable: Boolean,
    onForYouChange: (Boolean) -> Unit,
    sortType: SortType,
    onSortTypeChange: (SortType) -> Unit,
    searchScope: SearchScope,
    onSearchScopeChange: (SearchScope) -> Unit,
    areNotificationsAllowed: Boolean,
    theme: Theme,
    onThemeChange: (Theme) -> Unit,
    isDynamicColorEnabled: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    onFinishOnboarding: () -> Unit,
) {

    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { OnboardStage.entries.size })
    val scope = rememberCoroutineScope()


    LaunchedEffect(currentOnboardStage) {
        if (pagerState.currentPage != currentOnboardStage.ordinal) {
            pagerState.animateScrollToPage(currentOnboardStage.ordinal)
        }
    }


    val goNext = remember(pagerState) {
        {
            val nextIndex = pagerState.currentPage + 1
            if (nextIndex < OnboardStage.entries.size) {
                onStageChange(OnboardStage.entries[nextIndex])
            } else {
                onFinishOnboarding()
            }
        }
    }

    val goBack = remember(pagerState) {
        {
            val previousIndex = (pagerState.currentPage - 1).coerceAtLeast(0)
            onStageChange(OnboardStage.entries[previousIndex])
        }
    }

    val predictiveBackState = rememberPagerPredictiveBackState(
        pagerState = pagerState,
        onBack = { scope.launch { goBack() } }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        //if (isGranted) viewModel.enableNotifications(quietDurationMinutes = 60)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val progress by animateFloatAsState(
                targetValue = (pagerState.targetPage + 1) / OnboardStage.entries.size.toFloat(),
                animationSpec = tween(500),
                label = "ProgressBarAnimation"
            )

            LinearWavyProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                waveSpeed = 0.dp
            )

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.weight(1f)
            ) { page ->

                val currentOnboardStage = OnboardStage.entries[page]

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .predictiveBackPagerEffect(
                            page = page,
                            currentPage = pagerState.currentPage,
                            backState = predictiveBackState
                        )
                ) {
                    when (currentOnboardStage) {
                        OnboardStage.Welcome -> {
                            OnboardWelcome(onGetStartedClick = goNext)
                        }

                        OnboardStage.SignIn -> {
                            OnboardSignIn(
                                isSignedIn = isSignedIn,
                                onSignInClick = { context.launchSignIn() },
                                onGuestClick = {
                                    onSkipSignIn()
                                    goNext()
                                },
                                onContinueClick = goNext
                            )
                        }

                        OnboardStage.Notifications -> {
                            OnboardNotifications(
                                areNotificationsAllowed = areNotificationsAllowed,
                                onAllowClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        //viewModel.enableNotifications(quietDurationMinutes = 60)
                                    }
                                },
                                onSkipClick = goNext,
                                onContinueClick = goNext
                            )
                        }

                        OnboardStage.Appearance -> {
                            OnboardAppearance(
                                selectedTheme = theme,
                                dynamicColorEnabled = isDynamicColorEnabled,
                                onThemeSelected = onThemeChange,
                                onDynamicColorToggled = onDynamicColorChange,
                                onContinueClick = goNext
                            )
                        }

                        OnboardStage.Preferences -> {
                            OnboardPreferences(
                                isForYouAvailable = isForYouAvailable,
                                isForYouEnabled = isForYouEnabled,
                                sortType = sortType,
                                searchScope = searchScope,
                                onForYouChange = onForYouChange,
                                onSortTypeChange = onSortTypeChange,
                                onSearchScopeChange = onSearchScopeChange,
                                onFinishClick = goNext
                            )
                        }

                        OnboardStage.Finish -> {
                            OnboardFinish(onFinish = goNext)
                        }
                    }
                }
            }
        }
    }
}


@Composable
internal fun OnboardingHeroScreen(
    icon: ImageVector,
    iconTint: Color,
    containerColor: Color,
    title: String,
    description: String,
    primaryButtonText: String,
    primaryButtonIcon: ImageVector? = null,
    onPrimaryClick: () -> Unit,
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null
) {
    val haptics = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(containerColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = iconTint
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onPrimaryClick()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (primaryButtonIcon != null) {
                Icon(
                    imageVector = primaryButtonIcon,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(primaryButtonText)
        }

        if (secondaryButtonText != null && onSecondaryClick != null) {
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSecondaryClick()
                }, modifier = Modifier.fillMaxWidth()
            ) { Text(secondaryButtonText) }
        } else {
            Spacer(modifier = Modifier.height(16.dp)) // Maintain spacing if no secondary button
        }
    }
}