package com.kastik.buildlogic.conventions.plugins

import com.android.build.api.dsl.TestExtension
import com.kastik.buildlogic.conventions.AppConfig
import com.kastik.buildlogic.conventions.extensions.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

class AndroidBenchmarkConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        with(project) {

            with(pluginManager) {
                apply("com.android.test")
                apply("com.kastik.spotless")
            }

            extensions.configure<TestExtension> {
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
                }
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = AppConfig.sourceCompatibility
                targetCompatibility = AppConfig.targetCompatibility
            }

            configureKotlinJvm()
        }
    }
}