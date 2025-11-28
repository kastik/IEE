plugins {
    `kotlin-dsl`
}

group = "com.kastik.buildlogic.conventions"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("androidApplication") {
            id = "com.kastik.application"
            implementationClass =
                "com.kastik.buildlogic.conventions.application.AndroidApplicationConventionPlugin"
        }

        create("androidApplicationCompose") {
            id = "com.kastik.application.compose"
            implementationClass =
                "com.kastik.buildlogic.conventions.application.AndroidApplicationComposeConventionPlugin"
        }

        create("androidLibrary") {
            id = "com.kastik.library"
            implementationClass =
                "com.kastik.buildlogic.conventions.library.AndroidLibraryConventionPlugin"
        }

        create("androidLibraryCompose") {
            id = "com.kastik.library.compose"
            implementationClass =
                "com.kastik.buildlogic.conventions.library.AndroidLibraryComposeConventionPlugin"
        }

        create("hilt") {
            id = "com.kastik.hilt"
            implementationClass =
                "com.kastik.buildlogic.conventions.hilt.AndroidHiltConventionPlugin"
        }

        create("testLibrary") {
            id = "com.kastik.library.testing"
            implementationClass =
                "com.kastik.buildlogic.conventions.testing.AndroidLibraryTestingConventionPlugin"
        }
        create("testLibraryCompose") {
            id = "com.kastik.library.testing.compose"
            implementationClass =
                "com.kastik.buildlogic.conventions.testing.AndroidLibraryComposeTestingConventionPlugin"
        }

        create("benchmark") {
            id = "com.kastik.benchmark"
            implementationClass =
                "com.kastik.buildlogic.conventions.benchmark.BenchmarkConventionPlugin"
        }
    }
}