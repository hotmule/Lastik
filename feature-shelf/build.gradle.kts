plugins {
    id("lastik-component-mvi")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.sdk))
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))
                implementation(Libs.SqlDelight.coroutines)
            }
        }
    }
}