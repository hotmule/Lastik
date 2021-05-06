plugins {
    id("lastik-multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Libs.Kodein.common)
                implementation(Libs.Settings.common)
                implementation(Libs.Settings.coroutines)
                implementation(Libs.Kotlin.Coroutines.core)
            }
        }
    }
}