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

                implementation(project(Module.Feature.top))
                implementation(project(Module.Feature.shelf))
                implementation(project(Module.Feature.profile))
                implementation(project(Module.Feature.scrobbles))

                implementation(Libs.Kodein.common)
                implementation(Libs.Kotlin.Coroutines.core)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.MVIKotlin.coroutines)
            }
        }
    }
}