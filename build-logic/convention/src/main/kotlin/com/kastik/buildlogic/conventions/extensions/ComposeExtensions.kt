package com.kastik.buildlogic.conventions.extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

fun Project.configureAndroidCompose(
    extension: CommonExtension
) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
    pluginManager.apply("com.android.compose.screenshot")

    val libs = project.libs

    libs.findPlugin("stability-analyzer").ifPresent {
        pluginManager.apply(it.get().pluginId)
    }

    extension.apply {
        buildFeatures.compose = true
    }

    extensions.configure<CommonExtension> {
        experimentalProperties["android.experimental.enableScreenshotTest"] = true
    }


    extensions.configure<ComposeCompilerGradlePluginExtension> {
        stabilityConfigurationFiles.addAll(
            rootProject.layout.projectDirectory.file("stability_config.conf")
        )
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))

        add("implementation", libs.findLibrary("androidx-compose-ui").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        add("implementation", libs.findLibrary("androidx-compose-material3").get())
        add("implementation", libs.findLibrary("androidx-compose-foundation").get())
        add("implementation", libs.findLibrary("androidx-material-icons-extended").get())

        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())

        add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
        add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())

        add("screenshotTestImplementation", libs.findLibrary("screenshot-validation-api").get())
        add("screenshotTestImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        add("screenshotTestImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())

    }
}