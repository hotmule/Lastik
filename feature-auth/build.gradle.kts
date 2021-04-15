plugins {
    id("lastik-multiplatform")
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.utils))
                implementation(Libs.Kotlin.Coroutines.core)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.MVIKotlin.coroutines)
            }
        }
    }
}