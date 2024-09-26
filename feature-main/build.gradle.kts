plugins {
    id("lastik.component")
    id("kotlinx-serialization")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.featureScrobbles)
                implementation(projects.featureSettings)
            }
        }
    }
}
