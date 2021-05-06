plugins {
    id("lastik-component")
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