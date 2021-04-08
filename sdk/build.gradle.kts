plugins {
    id("lastik-multiplatform")
    id("com.squareup.sqldelight")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Libs.Krypto.common)
                implementation(Libs.Napier.common)
                implementation(Libs.Ktor.Core.common)
                implementation(Libs.Ktor.Auth.common)
                implementation(Libs.Ktor.Logging.common)
                implementation(Libs.Ktor.Serialization.common)
                implementation(Libs.SqlDelight.coroutines)
                implementation(Libs.Settings.common)
                implementation(Libs.Settings.coroutines)
                implementation(Libs.Kotlin.Coroutines.core)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(Libs.Krypto.android)
                implementation(Libs.Napier.android)
                implementation(Libs.Ktor.Core.android)
                implementation(Libs.Ktor.Auth.android)
                implementation(Libs.Ktor.Engine.android)
                implementation(Libs.Ktor.Logging.android)
                implementation(Libs.Ktor.Serialization.android)
                implementation(Libs.SqlDelight.Driver.android)
            }
        }
    }
}

sqldelight {
    database("LastikDatabase") {
        packageName = "ru.hotmule.lastik.data.local"
    }
}