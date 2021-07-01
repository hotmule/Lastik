plugins {
    id("lastik-component-mvi")
}

kotlin {
    sourceSets {
        named("androidMain") {
            dependencies {
                implementation(Libs.Kodein.android)
                implementation(Libs.AndroidX.core)
            }
        }
        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.remote))
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.sdk))
                implementation(Libs.Kotlin.dateTime)
            }
        }
    }
}