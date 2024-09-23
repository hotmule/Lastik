plugins {
    kotlin("jvm")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

dependencies {

    implementation(projects.uiCompose)
    implementation(projects.featureRoot)

    implementation(compose.desktop.currentOs)

    implementation(libs.kodein)
    implementation(libs.decompose)
}

compose.desktop {
    application {
        mainClass = "ru.hotmule.lastik.MainKt"
    }
}