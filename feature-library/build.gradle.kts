plugins {
    id("lastik.component.mvi")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.featureTop)
                implementation(projects.featureProfile)
                implementation(projects.featureScrobbles)
                implementation(projects.featureNowPlaying)
                implementation(libs.essenty.parcelable)
            }
        }
    }
}