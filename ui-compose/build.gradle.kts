import org.jetbrains.compose.compose

plugins {
    id("lastik-multiplatform")
    id("org.jetbrains.compose") version Libs.AndroidX.Compose.version
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {

                implementation(project(Module.Feature.root))
                implementation(project(Module.Feature.auth))
                implementation(project(Module.Feature.library))
                implementation(project(Module.Feature.profile))
                implementation(project(Module.Feature.shelf))

                implementation(compose.material)
                implementation(compose.materialIconsExtended)

                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.Decompose.composeExtensions)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(Libs.Accompanist.coil)
                implementation(Libs.Accompanist.swipeRefresh)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}