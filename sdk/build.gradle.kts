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
                implementation(Libs.Kermit.common)
                implementation(Libs.Ktor.Core.common)
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
                implementation(Libs.Ktor.Core.jvm)
                implementation(Libs.Ktor.Logging.jvm)
                implementation(Libs.Ktor.Engine.okhttp)
                implementation(Libs.Ktor.Serialization.jvm)
                implementation(Libs.SqlDelight.Driver.android)
            }
        }
        named("desktopMain") {
            dependencies {
                implementation(Libs.Ktor.Core.jvm)
                implementation(Libs.Ktor.Logging.jvm)
                implementation(Libs.Ktor.Engine.okhttp)
                implementation(Libs.Ktor.Serialization.jvm)
                implementation(Libs.SqlDelight.Driver.sqlite)
            }
        }
    }
}

sqldelight {
    database("LastikDatabase") {
        packageName = "ru.hotmule.lastik.data.local"
    }
}