package com.kastik.buildlogic.conventions.feature

import com.android.build.api.dsl.LibraryExtension
import com.kastik.buildlogic.conventions.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            apply(plugin = "com.kastik.library.compose")
            apply(plugin = "com.android.compose.screenshot")
            apply(plugin = "com.kastik.hilt")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
            val libs = project.libs

            dependencies {
                add("implementation", project(":core:domain"))
                add("implementation", project(":core:model"))
                add("implementation", project(":core:analytics"))
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:designsystem"))

                add(
                    "implementation",
                    libs.findLibrary("androidx-compose-material3-adaptive-navigation-suite").get()
                )
                add("implementation", libs.findLibrary("androidx-navigation-compose").get())
                add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())

                add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
                add(
                    "implementation",
                    libs.findLibrary("androidx-lifecycle-viewmodel-compose").get()
                )
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())

                add(
                    "screenshotTestImplementation",
                    libs.findLibrary("screenshot-validation-api").get()
                )
                add(
                    "screenshotTestImplementation",
                    libs.findLibrary("androidx-compose-ui-tooling").get()
                )
            }

            extensions.configure<LibraryExtension> {
                experimentalProperties["android.experimental.enableScreenshotTest"] = true
            }


        }
    }
}