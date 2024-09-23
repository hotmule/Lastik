plugins {
    id("lastik.component.mvi")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.dataLocal)
                implementation(projects.dataRemote)
                implementation(projects.featureShelf)
                implementation(projects.featureMenu)
                implementation(libs.sqldelight.coroutines)
            }
        }
    }
}