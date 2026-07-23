plugins {
    `kotlin-dsl`
}

group = "com.kastik.buildlogic.conventions"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.screenshot.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("androidApplication") {
            id = "com.kastik.application"
            implementationClass =
                "com.kastik.buildlogic.conventions.plugins.AndroidApplicationConventionPlugin"
        }

        create("androidLibrary") {
            id = "com.kastik.library"
            implementationClass =
                "com.kastik.buildlogic.conventions.plugins.AndroidLibraryConventionPlugin"
        }

        create("androidLibraryCompose") {
            id = "com.kastik.library.compose"
            implementationClass =
                "com.kastik.buildlogic.conventions.plugins.AndroidLibraryComposeConventionPlugin"
        }

        create("jvmLibrary") {
            id = "com.kastik.jvm.library"
            implementationClass =
                "com.kastik.buildlogic.conventions.plugins.JvmLibraryConventionPlugin"
        }

        create("feature") {
            id = "com.kastik.library.feature"
            implementationClass =
                "com.kastik.buildlogic.conventions.plugins.AndroidFeatureConventionPlugin"
        }

        create("hilt") {
            id = "com.kastik.hilt"
            implementationClass =
                "com.kastik.buildlogic.conventions.plugins.AndroidHiltConventionPlugin"
        }

        create("testLibrary") {
            id = "com.kastik.library.testing"
            implementationClass =
                "com.kastik.buildlogic.conventions.plugins.AndroidLibraryTestingConventionPlugin"
        }

        create("benchmark") {
            id = "com.kastik.benchmark"
            implementationClass =
                "com.kastik.buildlogic.conventions.plugins.AndroidBenchmarkConventionPlugin"
        }

        create("spotless") {
            id = "com.kastik.spotless"
            implementationClass =
                "com.kastik.buildlogic.conventions.plugins.SpotlessConventionPlugin"
        }

    }
}