package com.kastik.buildlogic.conventions.application

import com.android.build.api.dsl.ApplicationExtension
import com.kastik.buildlogic.conventions.config.AppConfig
import com.kastik.buildlogic.conventions.extensions.configureAndroidCompose
import com.kastik.buildlogic.conventions.extensions.configureFlavors
import com.kastik.buildlogic.conventions.extensions.configureKotlinJvm
import com.kastik.buildlogic.conventions.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
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

                testFixtures {
                    enable = true
                }

                configureFlavors(this)
                configureAndroidCompose(this)

            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = AppConfig.sourceCompatibility
                targetCompatibility = AppConfig.targetCompatibility
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.activity").get())
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-activity-compose").get())
                add("implementation", libs.findLibrary("kotlinx-collections-immutable").get())
                add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
            }
            configureKotlinJvm()
        }
    }
}
