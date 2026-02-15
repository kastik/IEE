import com.android.build.api.dsl.ApplicationExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kastik.application)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.gms)
    alias(libs.plugins.crashlytics.gradlePlugin)
    alias(libs.plugins.performance.gradlePlugin)
    alias(libs.plugins.baselineprofile)
}

configure<ApplicationExtension> {
    namespace = "com.kastik.apps"

    defaultConfig {
        applicationId = "com.kastik.apps"
        versionCode = 26
        versionName = "1.6.1"
    }

    val keystoreProperties = Properties().apply {
        val propsFile = rootProject.file("local.properties")
        if (propsFile.exists()) load(FileInputStream(propsFile))
    }

    signingConfigs {
        create("release") {
            val getSecret = { key: String, env: String ->
                (keystoreProperties[key] as? String) ?: System.getenv(env)
            }

            storeFile = file(getSecret("store.file", "RELEASE_STORE_FILE") ?: "release.jks")
            storePassword = getSecret("store.password", "RELEASE_STORE_PASSWORD")
            keyAlias = getSecret("key.alias", "RELEASE_KEY_ALIAS")
            keyPassword = getSecret("key.password", "RELEASE_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "true"
        }
        debug {
            applicationIdSuffix = ".debug"
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "false"
        }
    }

    productFlavors {
        named("local") {
            applicationIdSuffix = ".local"
            versionNameSuffix = "-local"
        }

        named("production") {
            isDefault = true
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:config"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":core:work"))
    implementation(project(":core:analytics"))
    implementation(project(":core:crashlytics"))
    implementation(project(":core:notifications"))
    implementation(project(":core:downloader"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:home"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:announcement"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:licenses"))
    implementation(project(":feature:search"))
    implementation(libs.androidx.work.runtime.ktx)

    baselineProfile(project(":benchmark"))
    implementation(libs.androidx.profileinstaller)
}

baselineProfile {
    saveInSrc = true
    mergeIntoMain = false
    dexLayoutOptimization = true
    automaticGenerationDuringBuild = false
}