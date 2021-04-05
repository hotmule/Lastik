plugins {
    id("kotlin-multiplatform")
    id("kotlin-parcelize")
}

kotlin {

    jvm("desktop")
    //android()

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Feature.auth))
                implementation(project(Module.Feature.main))
                implementation(Libs.Kotlin.Coroutines.core)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.Decompose.common)
            }
        }
    }
}