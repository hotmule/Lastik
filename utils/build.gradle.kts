plugins {
    id("lastik.multiplatform")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.mvikotlin)
                implementation(libs.decompose)
                implementation(libs.essenty.instance.keeper)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}