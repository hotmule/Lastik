plugins {
    id("lastik-multiplatform")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(Libs.Kotlin.Coroutines.common)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.Essenty.instanceKeeper)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(Libs.Kotlin.Coroutines.jvm)
            }
        }
    }
}