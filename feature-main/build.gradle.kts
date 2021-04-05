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
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.Decompose.common)
            }
        }
    }
}