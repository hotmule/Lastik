plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Libs.AndroidX.Compose.version
}

dependencies {

    implementation(project(Module.UI.compose))
    implementation(project(Module.Feature.root))

    implementation(compose.desktop.currentOs)

    implementation(Libs.Kodein.common)
    implementation(Libs.ArkIvanov.Decompose.common)
    implementation(Libs.ArkIvanov.Decompose.compose)
}

compose.desktop {
    application {
        mainClass = "ru.hotmule.lastik.MainKt"
    }
}