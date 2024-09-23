plugins {
    id("lastik.component.mvi")
    id("kotlinx-serialization")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.featureTop)
                implementation(projects.featureProfile)
                implementation(projects.featureScrobbles)
                implementation(projects.featureNowPlaying)
            }
        }
    }
}