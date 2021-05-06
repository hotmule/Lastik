plugins {
    id("lastik-component")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Feature.shelf))
            }
        }
    }
}