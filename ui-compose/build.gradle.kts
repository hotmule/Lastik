plugins {
    id("kotlin-multiplatform")
    id("org.jetbrains.compose") version "0.4.0-build177"
    id("com.android.library")
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
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.Decompose.composeExtensions)
            }
        }
    }
}

android {

    compileSdkVersion(Sdk.Version.compile)

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}