plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android.compileSdkVersion(Sdk.Version.compile)

dependencies {
    implementation(Deps.AndroidX.material)
}
