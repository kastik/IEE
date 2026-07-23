package com.kastik.buildlogic.conventions.plugins

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

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
                    ktfmt().googleStyle()
                }

                kotlinGradle {
                    target("*.gradle.kts")
                    targetExclude("**/build/**/*.gradle.kts")
                    ktfmt().googleStyle()
                }

                protobuf {
                    buf()
                }
            }
        }
    }
}