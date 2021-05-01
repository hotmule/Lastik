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
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))
                implementation(project(Module.Feature.auth))
                implementation(project(Module.Feature.library))

                implementation(Libs.Kotlin.Coroutines.core)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.MVIKotlin.coroutines)
            }
        }
    }
}