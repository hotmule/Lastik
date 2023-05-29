plugins {
    id("lastik-multiplatform")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(Libs.Kotlin.Coroutines.common)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(Libs.Kotlin.Coroutines.jvm)
            }
        }
    }
}
