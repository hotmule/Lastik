plugins {
    id("lastik.multiplatform")
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.androidx.core)
            }
        }
        commonMain {
            dependencies {
                implementation(projects.utils)
                implementation(libs.coil.core)
                implementation(libs.kodein)
                implementation(libs.settings)
                implementation(libs.settings.coroutines)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
