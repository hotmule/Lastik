plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.compose") version Libs.Compose.version
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

        buildConfigField(
            "String",
            "API_KEY",
            project.property("apiKey") as String
        )

        buildConfigField(
            "String",
            "SECRET",
            project.property("secret") as String
        )
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

    implementation(project(Module.UI.compose))
    implementation(project(Module.Feature.root))

    implementation(Libs.ArkIvanov.MVIKotlin.main)
    implementation(Libs.ArkIvanov.MVIKotlin.common)
    implementation(Libs.ArkIvanov.Decompose.common)
    implementation(Libs.ArkIvanov.Decompose.composeExtensions)

    implementation(compose.material)
    implementation(compose.materialIconsExtended)

    implementation(project(Module.sdk))

    implementation(Libs.Accompanist.coil)
    implementation(Libs.Accompanist.insets)

    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.Compose.activity)
    implementation(Libs.AndroidX.Compose.navigation)
    implementation(Libs.AndroidX.Compose.constraintLayout)
}