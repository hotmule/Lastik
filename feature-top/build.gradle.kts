plugins {
    id("lastik-component-mvi")
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.sdk))
                implementation(project(Module.Feature.shelf))
            }
        }
    }
}