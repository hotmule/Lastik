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

        minSdkVersion(Sdk.Version.min)
        targetSdkVersion(Sdk.Version.target)

        versionCode = App.Version.code
        versionName = App.Version.name

        testInstrumentationRunner = Deps.Testing.androidJUnitRunner
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(Proguard.rules)
        }
    }
}

dependencies {

}
