import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.dsl.TestExtension
import com.kastik.buildlogic.conventions.BuildDimensions
import com.kastik.buildlogic.conventions.BuildFlavors

plugins {
    alias(libs.plugins.kastik.benchmark)
    alias(libs.plugins.baselineprofile)
}

configure<TestExtension> {
    namespace = "com.kastik.apps.benchmark"

    defaultConfig {
        minSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        missingDimensionStrategy(BuildDimensions.ENVIRONMENT, BuildFlavors.PRODUCTION)
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    testOptions {
        managedDevices {
            localDevices {
                maybeCreate("pixel8api37").apply {
                    device = "Pixel 8"
                    apiLevel = 37
                    systemImageSource = "google"
                    testedAbi = "x86_64"
                    pageAlignment = ManagedVirtualDevice.PageAlignment.FORCE_16KB_PAGES
                }
            }
        }
    }
}

baselineProfile {
    managedDevices += "pixel8api37"
    useConnectedDevices = false
    skipBenchmarksOnEmulator = true
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro)
}
