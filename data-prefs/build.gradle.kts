plugins {
    id("lastik-multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(Libs.Settings.common)
                implementation(Libs.Settings.coroutines)
            }
        }
    }
}