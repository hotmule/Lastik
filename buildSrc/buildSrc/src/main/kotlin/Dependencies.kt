object App {

    const val id = "ru.hotmule.lastik"

    object Version {
        const val code = 1
        const val name = "1.0"
    }
}

object Sdk {
    object Version {
        const val min = 21
        const val target = 29
        const val compile = 29
        const val buildTools = "30.0.2"
    }
}

object Module {

    const val sdk = ":sdk"

    object Feature {
        const val root = ":feature-root"
        const val auth = ":feature-auth"
        const val main = ":feature-main"
    }

    object UI {
        const val compose = ":ui-compose"
    }
}

object Libs {

    const val gradle = "com.android.tools.build:gradle:4.1.1"
    const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
    const val jUnit = "junit:junit:4.13"

    object Kotlin {

        const val version = "1.4.32"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val serialization = "org.jetbrains.kotlin:kotlin-serialization:$version"

        object Coroutines {
            private const val version = "1.4.2"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        }
    }

    object Ktor {
        private const val version = "1.4.1"

        object Core {
            const val common = "io.ktor:ktor-client-core:$version"
            const val android = "io.ktor:ktor-client-core-jvm:$version"
        }

        object Auth {
            const val common = "io.ktor:ktor-client-auth:$version"
            const val android = "io.ktor:ktor-client-auth-jvm:$version"
        }

        object Engine {
            const val android = "io.ktor:ktor-client-okhttp:$version"
        }

        object Logging {
            const val common = "io.ktor:ktor-client-logging:$version"
            const val android = "io.ktor:ktor-client-logging-jvm:$version"
        }

        object Serialization {
            const val common = "io.ktor:ktor-client-serialization:$version"
            const val android = "io.ktor:ktor-client-serialization-jvm:$version"
        }
    }

    object ArkIvanov {

        object MVIKotlin {
            private const val version = "2.0.1"
            const val rx = "com.arkivanov.mvikotlin:rx:$version"
            const val common = "com.arkivanov.mvikotlin:mvikotlin:$version"
            const val main = "com.arkivanov.mvikotlin:mvikotlin-main:$version"
            const val coroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$version"
        }

        object Decompose {
            private const val version = "0.1.9"
            const val common = "com.arkivanov.decompose:decompose:$version"
            const val composeExtensions = "com.arkivanov.decompose:extensions-compose-jetbrains:$version"
        }
    }

    object SqlDelight {
        private const val version = "1.4.4"
        const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$version"
        const val coroutines = "com.squareup.sqldelight:coroutines-extensions:$version"

        object Driver {
            const val android = "com.squareup.sqldelight:android-driver:$version"
        }
    }

    object Napier {
        private const val version = "1.3.0"
        const val common = "com.github.aakira:napier:$version"
        const val android = "com.github.aakira:napier-android:$version"
    }

    object GradleVersions {
        const val version = "0.38.0"
        const val plugin = "com.github.ben-manes.versions"
    }

    object Krypto {
        private const val version = "2.0.6"
        const val common = "com.soywiz.korlibs.krypto:krypto:$version"
        const val android = "com.soywiz.korlibs.krypto:krypto-android:$version"
    }

    object Settings {
        private const val version = "0.7.3"
        const val common = "com.russhwolf:multiplatform-settings:$version"
        const val coroutines = "com.russhwolf:multiplatform-settings-coroutines:$version"
    }

    object Compose {
        const val version = "0.4.0-preview-annotation-build56"
    }

    object AndroidX {

        const val appCompat = "androidx.appcompat:appcompat:1.3.0-rc01"

        object Compose {

            const val version = "1.0.0-beta04"
            const val ui = "androidx.compose.ui:ui:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val material = "androidx.compose.material:material:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val icons = "androidx.compose.material:material-icons-extended:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"

            const val navigation = "androidx.navigation:navigation-compose:1.0.0-alpha10"
            const val activity = "androidx.activity:activity-compose:1.3.0-alpha06"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha05"
        }
    }

    object Accompanist {
        private const val version = "0.6.2"
        const val coil = "dev.chrisbanes.accompanist:accompanist-coil:$version"
        const val insets = "dev.chrisbanes.accompanist:accompanist-insets:$version"
    }
}

object Proguard {
    const val rules = "proguard-rules.pro"
    const val consumerRules = "consumer-rules.pro"
    const val default = "proguard-android-optimize.txt"
}