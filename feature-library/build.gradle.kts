plugins {
    id("lastik.component.mvi")
    id("kotlinx-serialization")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.featureTop)
                implementation(projects.featureMain)
                implementation(projects.featureProfile)
                implementation(projects.featureNowPlaying)
            }
        }
    }
}
