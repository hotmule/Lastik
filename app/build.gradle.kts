plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {

    compileSdkVersion(Version.Sdk.compile)
    buildToolsVersion(Version.Sdk.buildTools)

    defaultConfig {

        applicationId = App.id

        minSdkVersion(Version.Sdk.min)
        targetSdkVersion(Version.Sdk.target)

        versionCode = Version.App.code
        versionName = Version.App.name

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