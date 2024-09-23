plugins {
    id("lastik.multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(projects.dataSdk)
                implementation(libs.kodein)
                implementation(libs.krypto)
                implementation(libs.kermit)
                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.content.negotiation)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(libs.ktor.okhttp)
                implementation(libs.ktor.core.jvm)
                implementation(libs.ktor.logging.jvm)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(libs.ktor.okhttp)
                implementation(libs.ktor.core.jvm)
                implementation(libs.ktor.logging.jvm)
            }
        }
    }
}

android {

    defaultConfig {

        buildFeatures {
            buildConfig = true
        }

        buildConfigField(
            "String",
            "API_KEY",
            project.property("apiKey") as String
        )

        buildConfigField(
            "String",
            "SECRET",
            project.property("secret") as String
        )
    }
}