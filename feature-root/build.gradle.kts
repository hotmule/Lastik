plugins {
    id("lastik.component.mvi")
    id("kotlinx-serialization")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                
                implementation(projects.dataSdk)
                implementation(projects.dataLocal)
                implementation(projects.dataRemote)

                implementation(projects.featureAuth)
                implementation(projects.featureLibrary)

                implementation(libs.mvikotlin.main)
            }
        }
    }
}