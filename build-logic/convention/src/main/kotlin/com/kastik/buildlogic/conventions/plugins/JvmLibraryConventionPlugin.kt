package com.kastik.buildlogic.conventions.plugins

import com.kastik.buildlogic.conventions.extensions.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        with(project) {

            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("com.kastik.spotless")
            }

            configureKotlinJvm()
        }
    }
}