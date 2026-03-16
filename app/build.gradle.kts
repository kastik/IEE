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
        versionCode = 39
        versionName = "2.2.5"
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
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }

    productFlavors {
        named("local") {
            applicationIdSuffix = ".local"
            versionNameSuffix = "-local"
        }
    }
}

dependencies {
    implementation(project(":core:config"))
    implementation(project(":core:analytics"))
    implementation(project(":core:crashlytics"))
    implementation(project(":core:notifications"))

    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))

    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    implementation(project(":core:data"))
    implementation(project(":core:work"))
    implementation(project(":core:downloader"))

    implementation(project(":dev-tools"))

    implementation(project(":feature:home"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:announcement"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:licenses"))
    implementation(project(":feature:search"))

    baselineProfile(project(":benchmark"))
    implementation(libs.androidx.profileinstaller)
}

baselineProfile {
    saveInSrc = true
    mergeIntoMain = true
    dexLayoutOptimization = true
    automaticGenerationDuringBuild = false

    warnings {
        disabledVariants = false
    }
}