plugins {
    id("lastik-multiplatform")
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(Libs.Kotlin.Coroutines.core)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.Decompose.common)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(Libs.AndroidX.browser)
            }
        }
    }
}