package com.kastik.buildlogic.conventions.plugins

import com.android.build.api.dsl.LibraryExtension
import com.kastik.buildlogic.conventions.AppConfig
import com.kastik.buildlogic.conventions.extensions.configureKotlinJvm
import com.kastik.buildlogic.conventions.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.library")
                apply("com.kastik.spotless")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = AppConfig.COMPILE_SDK

                defaultConfig {
                    minSdk = AppConfig.MIN_SDK
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

            dependencies {
                add("implementation", libs.findLibrary("kotlinx-collections-immutable").get())
            }

            configureKotlinJvm()
        }
    }
}