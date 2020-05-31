plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Sdk.Version.compile)
    defaultConfig.minSdkVersion(Sdk.Version.min)
}

dependencies {
    implementation(Deps.AndroidX.material)
}
