plugins {
    id("lastik.component.mvi")
    id("kotlin-parcelize")
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
                implementation(libs.essenty.parcelable)
            }
        }
    }
}