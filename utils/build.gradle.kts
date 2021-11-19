plugins {
    id("lastik-multiplatform")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(Libs.Kotlin.coroutines)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.Essenty.instanceKeeper)
            }
        }
    }
}