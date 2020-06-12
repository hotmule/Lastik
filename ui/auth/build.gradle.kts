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
    implementation(Libs.AndroidX.Ui.framework)
    implementation(Libs.AndroidX.Ui.material)
    implementation(Libs.AndroidX.Ui.tooling)
    implementation(Libs.AndroidX.Ui.layout)
}
