plugins {
    id("lastik.multiplatform")
}

kotlin {
    sourceSets {
        
        named("commonMain") {
            dependencies {
                implementation(libs.kodein)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(libs.androidx.browser)
            }
        }
    }
}