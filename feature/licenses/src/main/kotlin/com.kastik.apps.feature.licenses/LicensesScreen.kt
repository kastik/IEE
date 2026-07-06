package com.kastik.apps.feature.licenses

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.extensions.TrackScreenViewEvent
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer


@Composable
internal fun LicensesRoute(
    viewModel: LicensesScreenViewModel = hiltViewModel()
) {

    TrackScreenViewEvent(
        screenClass = "licenses_route",
        screenName = "licenses_screen"
    )

    val libraries by produceLibraries(R.raw.aboutlibraries)

    LicensesScreen(libraries)
}

@Composable
private fun LicensesScreen(
    libraries: Libs? = null,
) {
    Scaffold { paddingValues ->
        LibrariesContainer(
            modifier = Modifier
                .fillMaxSize()
                .testTag("licences:licence_list"),
            libraries = libraries,
            contentPadding = paddingValues
        )
    }
}

@Preview
@Composable
private fun LicensesScreenPreview() {
    IeePreview {
        LicensesScreen()
    }
}