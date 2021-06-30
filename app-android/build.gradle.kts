plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.compose") version Libs.AndroidX.Compose.version
}

android {

    defaultConfig {

        applicationId = App.id
        versionCode = App.Version.code
        versionName = App.Version.name

        minSdk = Sdk.Version.min
        targetSdk = Sdk.Version.target
    }

    compileSdk = Sdk.Version.compile
    buildToolsVersion = Sdk.Version.buildTools
}

dependencies {
    
    implementation(project(Module.UI.compose))
    implementation(project(Module.Feature.root))
    implementation(project(Module.Feature.nowPlaying))

    implementation(compose.runtime)
    implementation(Libs.Kodein.android)
    implementation(Libs.Accompanist.insets)

    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.Compose.activity)

    implementation(Libs.ArkIvanov.Decompose.common)
    implementation(Libs.ArkIvanov.Decompose.compose)
}