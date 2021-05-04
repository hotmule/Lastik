plugins {
    id("lastik-multiplatform")
}

kotlin {
    sourceSets {
        
        named("commonMain") {
            dependencies {
                implementation(Libs.Kodein.common)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(Libs.AndroidX.browser)
            }
        }
    }
}