import org.jetbrains.compose.compose

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Libs.Compose.version
}

dependencies {

    implementation(project(Module.UI.compose))
    implementation(project(Module.Feature.root))

    implementation(Libs.ArkIvanov.Decompose.common)
    implementation(Libs.ArkIvanov.Decompose.composeExtensions)
    
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "ru.hotmule.lastik.MainKt"
    }
}