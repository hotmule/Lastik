plugins {
    id("lastik-multiplatform")
    id("org.jetbrains.compose") version Libs.AndroidX.Compose.version
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {

                implementation(project(Module.utils))

                implementation(project(Module.Feature.top))
                implementation(project(Module.Feature.root))
                implementation(project(Module.Feature.auth))
                implementation(project(Module.Feature.user))
                implementation(project(Module.Feature.menu))
                implementation(project(Module.Feature.shelf))
                implementation(project(Module.Feature.library))
                implementation(project(Module.Feature.profile))
                implementation(project(Module.Feature.settings))
                implementation(project(Module.Feature.scrobbles))
                implementation(project(Module.Feature.nowPlaying))

                implementation(compose.material)
                implementation(compose.materialIconsExtended)

                implementation(Libs.Kodein.compose)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.Decompose.compose)
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
                implementation(project(Module.utils))
                implementation(Libs.Ktor.Core.common)
                implementation(compose.desktop.currentOs)
            }
        }
    }
}