plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.Krypto.common)
                implementation(Libs.Napier.common)
                implementation(Libs.Ktor.Core.common)
                implementation(Libs.Ktor.Auth.common)
                implementation(Libs.Ktor.Logging.common)
                implementation(Libs.Ktor.Serialization.common)
                implementation(Libs.SqlDelight.coroutines)
                implementation(Libs.Settings.common)
                implementation(Libs.Settings.coroutines)
                implementation(Libs.Kotlin.redux)
                implementation(Libs.Kotlin.Coroutines.core)
                implementation(Libs.Decompose.common)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Libs.Krypto.android)
                implementation(Libs.Napier.android)
                implementation(Libs.Ktor.Core.android)
                implementation(Libs.Ktor.Auth.android)
                implementation(Libs.Ktor.Engine.android)
                implementation(Libs.Ktor.Logging.android)
                implementation(Libs.Ktor.Serialization.android)
                implementation(Libs.SqlDelight.Driver.android)
            }
        }
    }
}

android {
    compileSdkVersion(Sdk.Version.compile)
    defaultConfig {
        minSdkVersion(Sdk.Version.min)
        targetSdkVersion(Sdk.Version.target)
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        java.srcDirs("src/androidMain/kotlin")
        res.srcDirs("src/androidMain/res")
    }
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

sqldelight {
    database("LastikDatabase") {
        packageName = "ru.hotmule.lastik.data.local"
    }
}