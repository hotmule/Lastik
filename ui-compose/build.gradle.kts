plugins {
    id("kotlin-multiplatform")
    id("org.jetbrains.compose") version "0.4.0-build177"
    id("com.android.library")
}

repositories {
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

configurations {
    create("androidTestApi")
    create("androidTestDebugApi")
    create("androidTestReleaseApi")
    create("testApi")
    create("testDebugApi")
    create("testReleaseApi")
}

kotlin {

    jvm("desktop")
    android()

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Feature.root))
                implementation(project(Module.Feature.auth))
                implementation(project(Module.Feature.main))
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.material)
                implementation(compose.foundation)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.Decompose.composeExtensions)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(Libs.AndroidX.appCompat)
                implementation(Libs.AndroidX.Compose.activity)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
    }
}

android {

    compileSdkVersion(Sdk.Version.compile)
}