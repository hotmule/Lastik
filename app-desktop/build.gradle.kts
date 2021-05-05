import org.jetbrains.compose.compose

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Libs.AndroidX.Compose.version
}

dependencies {

    implementation(project(Module.Feature.root))
    implementation(project(Module.Data.remote))
    implementation(project(Module.Data.local))
    implementation(project(Module.Data.prefs))
    implementation(project(Module.UI.compose))
    implementation(project(Module.utils))

    implementation(Libs.Kodein.common)
    implementation(Libs.ArkIvanov.MVIKotlin.main)
    implementation(Libs.ArkIvanov.MVIKotlin.common)
    implementation(Libs.ArkIvanov.Decompose.common)
    implementation(Libs.ArkIvanov.Decompose.composeExtensions)
    
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "ru.hotmule.lastik.MainKt"
    }
}