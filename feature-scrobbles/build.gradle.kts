plugins {
    id("lastik-multiplatform")
    id("kotlin-parcelize")
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.prefs))
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))
                implementation(project(Module.Feature.shelf))
                implementation(Libs.Kodein.common)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
            }
        }
    }
}