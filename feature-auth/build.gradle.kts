plugins {
    id("lastik-multiplatform")
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.utils))
                implementation(project(Module.Data.prefs))
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))
                implementation(project(Module.Feature.browser))

                implementation(Libs.Kodein.common)
                implementation(Libs.Kotlin.Coroutines.core)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.MVIKotlin.coroutines)
            }
        }
    }
}