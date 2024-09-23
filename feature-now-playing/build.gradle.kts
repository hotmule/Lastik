plugins {
    id("lastik.component.mvi")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.kodein.android.core)
            }
        }
        commonMain {
            dependencies {
                implementation(projects.dataSdk)
                implementation(projects.dataLocal)
                implementation(projects.dataRemote)
                implementation(libs.kotlinx.datetime)
                implementation(compose.ui)
                implementation(libs.coil.core)
            }
        }
    }
}