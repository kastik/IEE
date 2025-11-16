package com.kastik.buildlogic.conventions.application

import com.android.build.api.dsl.ApplicationExtension
import com.kastik.buildlogic.conventions.common.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        with(project) {
            with(pluginManager) {
                apply("com.kastik.application")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}



