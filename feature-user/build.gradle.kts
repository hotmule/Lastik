plugins {
    id("lastik-component-mvi")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))
                implementation(project(Module.Feature.shelf))
                implementation(project(Module.Feature.menu))
                implementation(Libs.SqlDelight.coroutines)
            }
        }
    }
}
