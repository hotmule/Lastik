plugins {
    id("com.android.application")
    id("kotlin-android")
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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(Proguard.rules)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Libs.Kotlin.version
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }
}

dependencies {

    implementation(project(Module.sdk))
    implementation(project(Module.Android.Compose.utils))

    implementation(Libs.Accompanist.coil)
    implementation(Libs.Accompanist.insets)

    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.icons)
    implementation(Libs.AndroidX.Compose.layout)
    implementation(Libs.AndroidX.Compose.tooling)
    implementation(Libs.AndroidX.Compose.activity)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.foundation)
    implementation(Libs.AndroidX.Compose.navigation)
    implementation(Libs.AndroidX.Compose.constraintLayout)
}