plugins {
    id("lastik.component.mvi")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(projects.dataSdk)
                implementation(projects.dataLocal)
                implementation(projects.dataRemote)
                implementation(libs.sqldelight.coroutines)
            }
        }
    }
}