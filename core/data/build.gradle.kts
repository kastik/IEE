@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.library.testing)
    alias(libs.plugins.kastik.hilt)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.data"
}


dependencies {
    ksp(libs.room.compiler)

    implementation(project(":core:crashlytics"))
    implementation(project(":core:common"))
    implementation(project(":core:work"))
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(project(":core:notifications"))
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.room.runtime)
    implementation(libs.paging.common)
    implementation(libs.retrofit)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.kotlinx.datetime)

    testImplementation(testFixtures(project(":core:crashlytics")))
    testImplementation(testFixtures(project(":core:datastore")))
    testImplementation(testFixtures(project(":core:database")))
    testImplementation(testFixtures(project(":core:network")))
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.androidx.runner)
    testImplementation(libs.io.mockk)
    testImplementation(libs.robolectric)

    testFixturesImplementation(project(":core:network"))
    testFixturesImplementation(libs.androidx.test.core.ktx)
}