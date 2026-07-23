@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.api.dsl.LibraryExtension
import com.kastik.buildlogic.conventions.BuildDimensions
import com.kastik.buildlogic.conventions.BuildFlavors
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.library.testing)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.kotlinSerialization)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.network"

    flavorDimensions += BuildDimensions.ENVIRONMENT
    productFlavors {
        maybeCreate(BuildFlavors.PRODUCTION).apply {
            dimension = BuildDimensions.ENVIRONMENT
        }
        maybeCreate(BuildFlavors.LOCAL).apply {
            dimension = BuildDimensions.ENVIRONMENT
        }
    }

    testFixtures {
        enable = true
    }
}

dependencies {
    implementation(project(":core:crashlytics"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))

    api(libs.retrofit)
    api(libs.kotlinx.datetime)

    implementation(libs.retrofit.converter.scalars)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    testFixturesImplementation(project(":core:model"))
    testFixturesImplementation(libs.androidx.test.core.ktx)
    testFixturesImplementation(libs.okhttp.logging.interceptor)
    testFixturesImplementation(libs.kotlinx.datetime)
}
