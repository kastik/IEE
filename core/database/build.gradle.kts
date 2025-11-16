@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.library.testing)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.room)
    alias(libs.plugins.power.assert)
}
android {
    namespace = "com.kastik.apps.core.database"

    room {
        schemaDirectory("$projectDir/schemas")
    }

}
powerAssert {
    functions = listOf(
        "kotlin.assert",
        "kotlin.test.assertTrue",
        "kotlin.test.assertFalse",
        "kotlin.test.assertEquals",
        "kotlin.test.assertNull"
    )
    includedSourceSets = listOf("commonMain", "jvmMain", "jsMain", "nativeMain")
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