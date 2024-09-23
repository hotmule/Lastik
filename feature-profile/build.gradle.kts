plugins {
    id("lastik.component")
    id("kotlinx-serialization")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.featureUser)
                implementation(projects.featureSettings)
            }
        }
    }
}