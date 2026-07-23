package com.kastik.buildlogic.conventions.plugins

import com.kastik.buildlogic.conventions.extensions.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(plugin = "org.jetbrains.kotlin.jvm")
                apply("com.kastik.spotless")
            }

            configureKotlinJvm()
        }
    }
}