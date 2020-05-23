plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {

    compileSdkVersion(Sdk.Version.compile)

    defaultConfig {
        minSdkVersion(Sdk.Version.min)
        testInstrumentationRunner = Deps.AndroidX.jUnitRunner
    }
}

dependencies {
    testImplementation(Deps.jUnit)
    androidTestImplementation(Deps.AndroidX.jUnit)
}
