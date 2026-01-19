package com.kastik.buildlogic.conventions.extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

fun Project.configureAndroidCompose(
    extension: CommonExtension
) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    val libs = project.libs

    libs.findPlugin("stability-analyzer").ifPresent {
        pluginManager.apply(it.get().pluginId)
    }

    extension.apply {
        buildFeatures.compose = true
        composeOptions.kotlinCompilerExtensionVersion = "1.5.15"
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        stabilityConfigurationFiles.addAll(
            rootProject.layout.projectDirectory.file("stability_config.conf")
        )
        reportsDestination.set(layout.buildDirectory.dir("compose_compiler"))
        metricsDestination.set(layout.buildDirectory.dir("compose_compiler"))
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
        add("testImplementation", platform(bom))
        add("androidTestImplementation", platform(bom))

        add("implementation", libs.findLibrary("androidx-compose-ui").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())

        add("implementation", libs.findLibrary("androidx-compose-material3").get())

        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())

        add("implementation", libs.findLibrary("androidx-material-icons-extended").get())
    }
}