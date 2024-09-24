import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    id("lastik.multiplatform")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
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
    }
}

buildkonfig {
    packageName = "ru.hotmule.lastik.data.remote"
    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "apiKey",
            project.property("apiKey") as String
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "secret",
            project.property("secret") as String
        )
    }
}