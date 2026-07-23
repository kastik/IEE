package com.kastik.buildlogic.conventions.plugins

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.diffplug.spotless")
            }
            configure<SpotlessExtension> {
                java {
                    target("**/*.java")
                    targetExclude("**/build/**/*.java")
                    googleJavaFormat()
                }

                kotlin {
                    target("**/*.kt")
                    targetExclude("**/build/**/*.kt")
                    ktfmt().kotlinlangStyle()
                }

                kotlinGradle {
                    target("*.gradle.kts")
                    targetExclude("**/build/**/*.gradle.kts")
                    ktfmt().kotlinlangStyle()
                }

                protobuf {
                    buf("*")
                }
            }
        }
    }
}