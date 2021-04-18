plugins {
    id("lastik-multiplatform")
    id("kotlin-parcelize")
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Feature.auth))
                implementation(project(Module.Feature.main))
                implementation(project(Module.Data.prefs))
                implementation(project(Module.Data.remote))
                implementation(project(Module.utils))
                implementation(Libs.Kotlin.Coroutines.core)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.Decompose.common)
            }
        }
    }
}