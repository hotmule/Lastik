plugins {
    id("lastik-component-mvi")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(Module.Feature.top))
                implementation(project(Module.Feature.profile))
                implementation(project(Module.Feature.scrobbles))
            }
        }
    }
}