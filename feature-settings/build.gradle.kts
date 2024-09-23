plugins {
    id("lastik.component.mvi")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.dataSdk)
                implementation(libs.coil.core)
            }
        }
    }
}