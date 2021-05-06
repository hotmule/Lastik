plugins {
    id("lastik-component-mvi")
    id("kotlin-parcelize")
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.prefs))
                implementation(project(Module.Feature.shelf))
            }
        }
    }
}