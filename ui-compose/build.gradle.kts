import org.jetbrains.compose.compose

plugins {
    id("lastik-multiplatform")
    id("org.jetbrains.compose") version Libs.Compose.version
}

kotlin {

    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Feature.root))
                implementation(project(Module.Feature.auth))
                implementation(project(Module.Feature.main))
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.Decompose.composeExtensions)
            }
        }
        val androidMain by getting
        val desktopMain by getting
    }
}