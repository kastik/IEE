import com.android.build.api.dsl.TestExtension

plugins {
    alias(libs.plugins.kastik.benchmark)
    alias(libs.plugins.baselineprofile)
}

configure<TestExtension> {
    namespace = "com.kastik.apps.benchmark"

    defaultConfig {
        minSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    testOptions {
        managedDevices {
            localDevices {
                maybeCreate("pixel6Api34").apply {
                    device = "Pixel 6"
                    apiLevel = 34
                    testedAbi = "x86_64"
                    systemImageSource = "aosp-atd"
                    require64Bit = true
                }
            }
        }
    }
}

baselineProfile {
    managedDevices += "pixel6Api34"
    useConnectedDevices = true
    skipBenchmarksOnEmulator = true
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro)
}