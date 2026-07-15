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
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer


@Composable
internal fun LicensesRoute(
    viewModel: LicensesViewModel = hiltViewModel()
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
internal fun LicensesScreenPreview() {

    val fakeLibs = Libs(
        libraries = listOf(
            Library(
                uniqueId = "com.example.fake:awesome-lib",
                artifactVersion = "1.2.3",
                name = "Awesome Fake Lib",
                description = "This is a fantastic fake library used for previewing UI components.",
                website = "https://example.com/awesome-lib",
                developers = emptyList(), // Fixed: Expects List<Developer>
                organization = null,
                scm = null,
                licenses = setOf(
                    License(
                        hash = "apache_2_0", // This links to the string above
                        name = "Apache License 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt",
                        year = "2024",
                        licenseContent = "Standard Apache 2.0 License content here..." // Fixed: Renamed from content
                    )
                ), // Fixed: Expects Set<String> (license hashes)
                funding = emptySet(),
                tag = null
            )
        ),
        licenses = setOf(
            License(
                hash = "apache_2_0", // This links to the string above
                name = "Apache License 2.0",
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt",
                year = "2024",
                licenseContent = "Standard Apache 2.0 License content here..." // Fixed: Renamed from content
            )
        )
    )

    IeePreview {
        LicensesScreen(libraries = fakeLibs)
    }
}