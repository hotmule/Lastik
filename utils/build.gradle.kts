plugins {
    id("lastik-multiplatform")
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(Libs.ArkIvanov.MVIKotlin.rx)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.Decompose.common)
            }
        }
    }
}