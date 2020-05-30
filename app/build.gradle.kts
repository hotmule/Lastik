plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
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

    kotlinOptions.jvmTarget = "1.8"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(Proguard.rules)
        }
    }
}

dependencies {

    implementation(project(Module.Ui.auth))
    implementation(project(Module.shared))

    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.Navigation.fragment)
}