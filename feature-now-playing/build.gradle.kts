plugins {
    id("lastik.component.mvi")
}

kotlin {
    sourceSets {
        named("androidMain") {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.kodein.android.core)
            }
        }
        named("commonMain") {
            dependencies {
                implementation(projects.dataSdk)
                implementation(projects.dataLocal)
                implementation(projects.dataRemote)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}