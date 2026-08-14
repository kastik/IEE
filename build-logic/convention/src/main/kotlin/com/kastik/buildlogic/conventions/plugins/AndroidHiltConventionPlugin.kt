package com.kastik.buildlogic.conventions.plugins

import com.kastik.buildlogic.conventions.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        with(project) {

            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
            }

            dependencies {
                "implementation"(libs.findLibrary("hilt-android").get())
                "ksp"(libs.findLibrary("androidx-hilt-compiler").get())
                "ksp"(libs.findLibrary("hilt-android.compiler").get())
            }

        }
    }

}