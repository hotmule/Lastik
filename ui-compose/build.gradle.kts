plugins {
    id("lastik.multiplatform")
    alias(libs.plugins.compose)
    //alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {

                implementation(projects.utils)

                implementation(projects.featureTop)
                implementation(projects.featureRoot)
                implementation(projects.featureAuth)
                implementation(projects.featureUser)
                implementation(projects.featureMenu)
                implementation(projects.featureShelf)
                implementation(projects.featureLibrary)
                implementation(projects.featureProfile)
                implementation(projects.featureSettings)
                implementation(projects.featureScrobbles)
                implementation(projects.featureNowPlaying)

                implementation(compose.material)
                implementation(compose.materialIconsExtended)

                implementation(libs.decompose)
                implementation(libs.decompose.compose)
                implementation(libs.kodein.compose)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(libs.coil)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(projects.utils)
                implementation(compose.desktop.currentOs)
            }
        }
    }
}