plugins {
    id("lastik-component")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(Module.Feature.user))
                implementation(project(Module.Feature.settings))
                implementation(Libs.ArkIvanov.Essenty.parcelable)
            }
        }
    }
}