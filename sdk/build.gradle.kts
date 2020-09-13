plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

kotlin {
    android()
    sourceSets {
        commonMain {
            dependencies {
                implementation(Libs.preferences)
                implementation(Libs.Napier.common)
                implementation(Libs.Ktor.Core.common)
                implementation(Libs.Ktor.Auth.common)
                implementation(Libs.Ktor.Logging.common)
                implementation(Libs.Ktor.Serialization.common)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Libs.Napier.android)
                implementation(Libs.Ktor.Core.android)
                implementation(Libs.Ktor.Auth.android)
                implementation(Libs.Ktor.Engine.android)
                implementation(Libs.Ktor.Logging.android)
                implementation(Libs.Ktor.Serialization.android)
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
}