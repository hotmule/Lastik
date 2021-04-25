plugins {
    id("lastik-multiplatform")
    id("kotlin-parcelize")
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.utils))
                implementation(project(Module.Data.prefs))
                implementation(project(Module.Feature.shelf))
                implementation(Libs.Kotlin.Coroutines.core)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.MVIKotlin.coroutines)
            }
        }
    }
}