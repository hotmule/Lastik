plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.compose") version Libs.AndroidX.Compose.version
}

android {

    compileSdkVersion(Sdk.Version.compile)
    buildToolsVersion(Sdk.Version.buildTools)

    defaultConfig {

        applicationId = App.id
        versionCode = App.Version.code
        versionName = App.Version.name

        minSdkVersion(Sdk.Version.min)
        targetSdkVersion(Sdk.Version.target)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    
    implementation(project(Module.utils))
    implementation(project(Module.UI.compose))
    implementation(project(Module.Feature.root))
    implementation(project(Module.Feature.scrobbler))

    implementation(compose.runtime)
    implementation(Libs.Kodein.android)
    implementation(Libs.Accompanist.insets)
    implementation(Libs.Kotlin.Coroutines.core)

    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.Compose.activity)

    implementation(Libs.ArkIvanov.Decompose.common)
    implementation(Libs.ArkIvanov.Decompose.compose)
}