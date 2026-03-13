package com.kastik.buildlogic.conventions.hilt

import com.kastik.buildlogic.conventions.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
            }

            val libs = project.libs
            dependencies {
                "implementation"(libs.findLibrary("hilt-android").get())
                "implementation"(libs.findLibrary("androidx-hilt-work").get())
                "implementation"(libs.findLibrary("androidx-hilt-common").get())
                "ksp"(libs.findLibrary("androidx-hilt-compiler").get())
                "ksp"(libs.findLibrary("hilt-android.compiler").get())
            }

        }
    }

}