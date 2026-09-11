package com.kastik.buildlogic.conventions.plugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.kastik.buildlogic.conventions.AppConfig
import com.kastik.buildlogic.conventions.extensions.configureAndroidCompose
import com.kastik.buildlogic.conventions.extensions.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        with(project) {

            with(pluginManager) {
                apply("com.android.application")
                apply("com.kastik.spotless")
                apply("com.kastik.detekt")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = AppConfig.COMPILE_SDK

                defaultConfig {
                    minSdk = AppConfig.MIN_SDK
                    targetSdk = AppConfig.TARGET_SDK
                }
                compileOptions {
                    sourceCompatibility = AppConfig.sourceCompatibility
                    targetCompatibility = AppConfig.targetCompatibility
                }
                lint {
                    showAll = true
                    abortOnError = true
                    warningsAsErrors = true

                    informational.addAll(
                        listOf(
                            "AndroidGradlePluginVersion",
                            "GradleDependency",
                            "NewerVersionAvailable"
                        )
                    )
                }

                configureAndroidCompose(this)

            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = AppConfig.sourceCompatibility
                targetCompatibility = AppConfig.targetCompatibility
            }

            val androidComponents = extensions.getByType<ApplicationAndroidComponentsExtension>()

            androidComponents.onVariants { variant ->
                val projectName = project.rootProject.name.lowercase()
                val moduleName = project.name.lowercase()

                val appVersionName = variant.outputs.first().versionName.getOrElse("unknown")
                val appVersionCode = variant.outputs.first().versionCode.getOrElse(0)

                extensions.configure<BasePluginExtension> {
                    archivesName.set("$projectName-$moduleName-$appVersionName-($appVersionCode)")
                }
            }

            configureKotlinJvm()
        }
    }
}