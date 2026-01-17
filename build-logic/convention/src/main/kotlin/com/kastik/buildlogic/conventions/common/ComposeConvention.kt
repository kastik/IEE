package com.kastik.buildlogic.conventions.common

import com.android.build.api.dsl.CommonExtension
import com.kastik.buildlogic.conventions.config.AppConfig
import com.kastik.buildlogic.conventions.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

fun Project.configureAndroidCompose(
    extension: CommonExtension
) {
    val libs = project.libs

    extension.apply {
        buildFeatures.compose = true
        composeOptions.kotlinCompilerExtensionVersion = "1.5.15"
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
        add("testImplementation", platform(bom))
        add("androidTestImplementation", platform(bom))

        // Core UI Runtime
        add("implementation", libs.findLibrary("androidx-compose-ui").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())

        // Material
        add("implementation", libs.findLibrary("androidx-compose-material3").get())

        // Tooling
        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())

        // Icons
        add("implementation", libs.findLibrary("androidx-material-icons-extended").get())
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(AppConfig.jvmTarget)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.addAll(buildComposeMetricsParameters())
        }
    }
}