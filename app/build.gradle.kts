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

    implementation(project(Module.shared))

    implementation(Deps.Kotlin.stdLibJdk8)

    implementation(Deps.AndroidX.core)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.constraintLayout)

    testImplementation(Deps.Testing.jUnit)
    androidTestImplementation(Deps.Testing.androidJUnit)
    androidTestImplementation(Deps.Testing.espresso)
}