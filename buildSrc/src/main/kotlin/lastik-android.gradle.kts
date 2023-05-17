plugins {
    id("com.android.library")
}

android {

    compileSdk = Sdk.Version.compile

    defaultConfig {
        minSdk = Sdk.Version.min
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}