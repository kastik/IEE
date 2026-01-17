package com.kastik.buildlogic.conventions.testing

import com.kastik.buildlogic.conventions.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class AndroidLibraryTestingConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            plugins.withId("com.android.library") {
                dependencies {
                    add("testImplementation", libs.findLibrary("kotlin-test").get())
                    add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                    add("testImplementation", libs.findLibrary("androidx-test-core").get())
                    add("testImplementation", libs.findLibrary("androidx-test-core-ktx").get())
                    add("testImplementation", libs.findLibrary("androidx-junit").get())
                    add("testImplementation", libs.findLibrary("androidx-truth").get())
                    add("testImplementation", libs.findLibrary("junit-platform-suite-engine").get())
                }
            }
            tasks.withType<Test>().configureEach {
                jvmArgs("-XX:+EnableDynamicAgentLoading")
            }
        }
    }
}