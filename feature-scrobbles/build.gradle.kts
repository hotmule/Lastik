plugins {
    id("lastik-component")
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