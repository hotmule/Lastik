plugins {
    id("com.android.library")
}

android {

    compileSdkVersion(Sdk.Version.compile)

    defaultConfig {
        minSdkVersion(Sdk.Version.min)
        targetSdkVersion(Sdk.Version.target)
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}