plugins {
    id("lastik.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {

                implementation(projects.utils)

                implementation(projects.featureTop)
                implementation(projects.featureRoot)
                implementation(projects.featureAuth)
                implementation(projects.featureMain)
                implementation(projects.featureShelf)
                implementation(projects.featureLibrary)
                implementation(projects.featureProfile)
                implementation(projects.featureSettings)
                implementation(projects.featureScrobbles)
                implementation(projects.featureNowPlaying)

                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.resources)

                implementation(libs.coil.ktor)
                implementation(libs.coil.compose)

                implementation(libs.decompose)
                implementation(libs.decompose.compose)
                implementation(libs.kodein.compose)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
