package com.kastik.buildlogic.conventions.testing

import com.kastik.buildlogic.conventions.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

//TODO Find a way to configure power assert here
class AndroidLibraryTestingConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            plugins.withId("com.android.library") {
                dependencies {
                    add("testImplementation", libs.findLibrary("junit").get())
                    add("testImplementation", libs.findLibrary("junit-platform-suite-engine").get())
                    add("testImplementation", libs.findLibrary("kotlin-test").get())
                    add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                    add("testImplementation", libs.findLibrary("androidx-junit").get())
                    add("testImplementation", libs.findLibrary("androidx-test-core").get())
                }
            }
        }
    }
}