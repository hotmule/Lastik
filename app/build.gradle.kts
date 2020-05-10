plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {

    compileSdkVersion(SdkVersion.compile)
    buildToolsVersion(SdkVersion.buildTools)

    defaultConfig {

        applicationId = App.id

        minSdkVersion(SdkVersion.min)
        targetSdkVersion(SdkVersion.target)

        versionCode = AppVersion.code
        versionName = AppVersion.name

        testInstrumentationRunner = Deps.androidJUnitRunner
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(Proguard.rules)
        }
    }
}

dependencies {

    implementation(Deps.kotlin)

    implementation(Deps.core)
    implementation(Deps.appCompat)
    implementation(Deps.constraintLayout)

    testImplementation(Deps.jUnit)
    androidTestImplementation(Deps.androidJUnit)
    androidTestImplementation(Deps.espresso)
}