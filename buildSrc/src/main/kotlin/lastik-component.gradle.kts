plugins {
    id("lastik-multiplatform")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(Libs.Kodein.common)
                implementation(Libs.ArkIvanov.Decompose.common)
            }
        }
    }
}