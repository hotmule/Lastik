plugins {
    id("lastik.component")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(projects.featureShelf)
            }
        }
    }
}