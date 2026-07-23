@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.api.dsl.LibraryExtension
import com.kastik.buildlogic.conventions.BuildDimensions
import com.kastik.buildlogic.conventions.BuildFlavors
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.library.testing)
    alias(libs.plugins.kastik.hilt)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.data"

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
    ksp(libs.room.compiler)

    implementation(project(":core:crashlytics"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(libs.androidx.lifecycle.process)

    testImplementation(testFixtures(project(":core:crashlytics")))
    testImplementation(testFixtures(project(":core:datastore")))
    testImplementation(testFixtures(project(":core:database")))
    testImplementation(testFixtures(project(":core:network")))
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.androidx.runner)
    testImplementation(libs.io.mockk)
    testImplementation(libs.robolectric)
}
