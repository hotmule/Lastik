plugins {
    id("lastik-multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.sdk))
                implementation(Libs.Kodein.common)
                implementation(Libs.Krypto.common)
                implementation(Libs.Kermit.common)
                implementation(Libs.Ktor.Core.common)
                implementation(Libs.Ktor.Logging.common)
                implementation(Libs.Ktor.Serialization.json)
                implementation(Libs.Ktor.Serialization.contentNegotiation)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(Libs.Ktor.Core.jvm)
                implementation(Libs.Ktor.Logging.jvm)
                implementation(Libs.Ktor.Engine.okhttp)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(Libs.Ktor.Core.jvm)
                implementation(Libs.Ktor.Logging.jvm)
                implementation(Libs.Ktor.Engine.okhttp)
            }
        }

        named("iosSimulatorArm64Main") {
            dependencies {
                implementation(Libs.Ktor.Engine.darwin)
            }
        }
    }
}

android {

    defaultConfig {

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
