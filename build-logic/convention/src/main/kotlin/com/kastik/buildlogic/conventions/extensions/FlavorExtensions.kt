package com.kastik.buildlogic.conventions.extensions

import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryProductFlavor
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.variant.TestAndroidComponentsExtension
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
                when (this) {
                    is ApplicationProductFlavor -> isDefault = true
                    is LibraryProductFlavor -> isDefault = true
                }
            }
        }
    }

    extensions.findByType(ApplicationAndroidComponentsExtension::class.java)?.apply {
        beforeVariants {
            it.enable = it.name !in listOf(
                "localBenchmarkRelease",
                "localNonMinifiedRelease",
                "localRelease"
            )
        }
    }

    extensions.findByType(LibraryAndroidComponentsExtension::class.java)?.apply {
        beforeVariants {
            it.enable = it.name != "localRelease"
        }
    }

    extensions.findByType(TestAndroidComponentsExtension::class.java)?.apply {
        beforeVariants {
            it.enable = it.flavorName == "production"
        }
    }
}