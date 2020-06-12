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
        versionCode = App.Version.code
        versionName = App.Version.name

        minSdkVersion(Sdk.Version.min)
        targetSdkVersion(Sdk.Version.target)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = "1.8"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(Proguard.rules)
        }
    }

    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerVersion = "1.3.70-dev-withExperimentalGoogleExtensions-20200424"
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }
}

dependencies {

    implementation(project(Module.Ui.auth))
    implementation(project(Module.Ui.scrobbles))
    implementation(project(Module.Ui.profile))

    implementation(Libs.AndroidX.Compose.foundation)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.tooling)
    implementation(Libs.AndroidX.Compose.layout)

    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.Navigation.ui)
    implementation(Libs.AndroidX.Navigation.fragment)
}