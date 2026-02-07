package com.kastik.buildlogic.conventions.extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

fun Project.configureFlavors(
    extension: CommonExtension
) {
    extension.apply {
        flavorDimensions += "environment"

        productFlavors {
            maybeCreate("local").apply {
                dimension = "environment"
            }
            maybeCreate("production").apply {
                dimension = "environment"
            }
        }
    }
}