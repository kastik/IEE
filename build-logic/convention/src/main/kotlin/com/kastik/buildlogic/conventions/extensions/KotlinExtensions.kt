package com.kastik.buildlogic.conventions.extensions

import com.kastik.buildlogic.conventions.AppConfig
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

fun Project.configureKotlinJvm() {
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(AppConfig.jvmTarget)
            freeCompilerArgs.addAll(
                listOf(
                    "-Xreturn-value-checker=check",
                )
            )
        }
    }
}