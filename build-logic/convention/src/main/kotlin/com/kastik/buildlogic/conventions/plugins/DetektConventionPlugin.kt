package com.kastik.buildlogic.conventions.plugins

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.extensions.FailOnSeverity
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        with(project) {

            with(pluginManager) {
                apply("dev.detekt")
            }

            configure<DetektExtension> {
                parallel.set(true)
                autoCorrect.set(true)
                ignoreFailures.set(false)
                failOnSeverity.set(FailOnSeverity.Warning)
                buildUponDefaultConfig.set(true)
                basePath.set(rootProject.layout.projectDirectory)
                config.setFrom("${rootProject.layout.projectDirectory}/detekt.yml")
            }

            tasks.withType<Detekt>().configureEach {
                reports {
                    markdown.required.set(true)
                    sarif.required.set(true)
                }
            }
        }
    }
}