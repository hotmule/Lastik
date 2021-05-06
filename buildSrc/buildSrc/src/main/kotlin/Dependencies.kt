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

    const val utils = ":utils"

    object Feature {
        const val root = ":feature-root"
        const val auth = ":feature-auth"
        const val library = ":feature-library"
        const val top = ":feature-top"
        const val profile = ":feature-profile"
        const val scrobbles = ":feature-scrobbles"
        const val shelf = ":feature-shelf"
        const val browser = ":feature-browser"
    }

    object Data {
        const val prefs = ":data-prefs"
        const val local = ":data-local"
        const val remote = ":data-remote"
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
        private const val version = "1.5.4"

        object Core {
            const val common = "io.ktor:ktor-client-core:$version"
            const val jvm = "io.ktor:ktor-client-core-jvm:$version"
        }

        object Logging {
            const val common = "io.ktor:ktor-client-logging:$version"
            const val jvm = "io.ktor:ktor-client-logging-jvm:$version"
        }

        object Serialization {
            const val common = "io.ktor:ktor-client-serialization:$version"
            const val jvm = "io.ktor:ktor-client-serialization-jvm:$version"
        }

        object Engine {
            const val okhttp = "io.ktor:ktor-client-okhttp:$version"
        }
    }

    object ArkIvanov {

        object MVIKotlin {
            private const val version = "2.0.3"
            const val common = "com.arkivanov.mvikotlin:mvikotlin:$version"
            const val main = "com.arkivanov.mvikotlin:mvikotlin-main:$version"
            const val coroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$version"
        }

        object Decompose {
            private const val version = "0.2.4"
            const val common = "com.arkivanov.decompose:decompose:$version"
            const val jvm = "com.arkivanov.decompose:decompose-jvm:$version"
            const val composeExtensions = "com.arkivanov.decompose:extensions-compose-jetbrains:$version"
        }
    }

    object SqlDelight {
        private const val version = "1.5.0"
        const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$version"
        const val coroutines = "com.squareup.sqldelight:coroutines-extensions:$version"

        object Driver {
            const val android = "com.squareup.sqldelight:android-driver:$version"
            const val sqlite = "com.squareup.sqldelight:sqlite-driver:$version"
        }
    }

    object Kodein {
        private const val version = "7.5.0"
        const val common = "org.kodein.di:kodein-di:$version"
        const val compose = "org.kodein.di:kodein-di-framework-compose:$version"
        const val android = "org.kodein.di:kodein-di-framework-android-core:$version"
    }

    object Kermit {
        private const val version = "0.1.8"
        const val common = "co.touchlab:kermit:$version"
    }

    object GradleVersions {
        const val version = "0.38.0"
        const val plugin = "com.github.ben-manes.versions"
    }

    object Krypto {
        private const val version = "2.0.7"
        const val common = "com.soywiz.korlibs.krypto:krypto:$version"
        const val android = "com.soywiz.korlibs.krypto:krypto-android:$version"
    }

    object Settings {
        private const val version = "0.7.6"
        const val common = "com.russhwolf:multiplatform-settings:$version"
        const val coroutines = "com.russhwolf:multiplatform-settings-coroutines:$version"
    }

    object AndroidX {

        const val browser = "androidx.browser:browser:1.3.0"
        const val appCompat = "androidx.appcompat:appcompat:1.3.0-rc01"

        object Compose {
            const val version = "0.4.0-build188"
            const val activity = "androidx.activity:activity-compose:1.3.0-alpha07"
        }
    }

    object Accompanist {
        private const val version = "0.9.0"
        const val coil = "com.google.accompanist:accompanist-coil:$version"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
    }
}