plugins {
    id("lastik.component")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.featureUser)
                implementation(projects.featureSettings)
                implementation(libs.essenty.parcelable)
            }
        }
    }
}