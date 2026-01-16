@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.library.testing)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.room)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.database"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(project(":core:model"))

    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(project(":core:domain"))
    testImplementation(project(":core:testing"))

}