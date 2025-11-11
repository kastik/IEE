package com.kastik.apps.library

import com.android.build.gradle.LibraryExtension
import com.kastik.apps.config.AppConfig
import com.kastik.apps.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("maven-publish")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = AppConfig.COMPILE_SDK

                defaultConfig {
                    minSdk = AppConfig.MIN_SDK
                    targetSdk = AppConfig.TARGET_SDK
                }
                compileOptions {
                    sourceCompatibility = AppConfig.sourceCompatibility
                    targetCompatibility = AppConfig.targetCompatibility
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
            }
            tasks.withType<KotlinJvmCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(AppConfig.jvmTarget)
                    freeCompilerArgs.addAll(
                        listOf(
                            "-opt-in=kotlin.RequiresOptIn",
                            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                            "-opt-in=kotlinx.coroutines.FlowPreview"
                        )
                    )
                }
            }
        }
    }
}