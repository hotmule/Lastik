plugins {
    id("lastik.multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.utils)
                implementation(libs.kodein)
                implementation(libs.settings)
                implementation(libs.settings.coroutines)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}