package com.kastik.buildlogic.conventions.testing

import com.kastik.buildlogic.conventions.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryComposeTestingConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            dependencies {
                add("testImplementation", libs.findLibrary("robolectric").get())
                add("testImplementation", libs.findLibrary("androidx-compose-ui-junit").get())
                add("testImplementation", libs.findLibrary("androidx-compose-ui-manifest").get())
            }
        }
    }
}