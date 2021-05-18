plugins {
    id("lastik-component-mvi")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {

                implementation(project(Module.Data.prefs))
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))

                implementation(project(Module.Feature.shelf))
                implementation(project(Module.Feature.settings))

                implementation(Libs.SqlDelight.coroutines)
            }
        }
    }
}