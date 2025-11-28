plugins {
    alias(libs.plugins.kastik.application.compose)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gms)
    alias(libs.plugins.crashlytics.gradlePlugin)
    alias(libs.plugins.performance.gradlePlugin)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.kastik.apps"

    defaultConfig {
        applicationId = "com.kastik.apps"
        versionCode = 4
        versionName = "1.0"
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            baselineProfile.automaticGenerationDuringBuild = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":feature:home"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:announcement"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:search"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(libs.androidx.activity)
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.performance) {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.androidx.profileinstaller)
    baselineProfile(project(":benchmark"))
}
