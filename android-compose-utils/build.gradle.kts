plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {

    compileSdkVersion(Sdk.Version.compile)
    buildToolsVersion(Sdk.Version.buildTools)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Libs.Kotlin.version
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }
}

dependencies {
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.foundation)
}