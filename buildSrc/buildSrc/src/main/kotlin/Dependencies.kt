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
        const val target = 33
        const val compile = 33
        const val buildTools = "33.0.2"
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
        const val nowPlaying = ":feature-now-playing"
        const val settings = ":feature-settings"
        const val user = ":feature-user"
        const val menu = ":feature-menu"
    }

    object Data {
        const val sdk = ":data-sdk"
        const val local = ":data-local"
        const val remote = ":data-remote"
    }

    object UI {
        const val compose = ":ui-compose"
    }
}

object Libs {

    const val gradle = "com.android.tools.build:gradle:7.4.1"

    object Kotlin {
        const val version = "1.8.20"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"

        object Serialization {
            const val common = "org.jetbrains.kotlin:kotlin-serialization:$version"
            const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1"
        }

        object Coroutines {
            const val version = "1.7.1"
            const val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1"
            const val jvm = "org.jetbrains.kotlinx:kotlinx-coroutines-swing:$version"
        }
    }

    object Ktor {
        private const val version = "2.3.0"

        object Core {
            const val common = "io.ktor:ktor-client-core:$version"
            const val jvm = "io.ktor:ktor-client-core-jvm:$version"
        }

        object Logging {
            const val common = "io.ktor:ktor-client-logging:$version"
            const val jvm = "io.ktor:ktor-client-logging-jvm:$version"
        }

        object Serialization {
            const val json = "io.ktor:ktor-serialization-kotlinx-json:$version"
            const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:$version"
        }

        object Engine {
            const val okhttp = "io.ktor:ktor-client-okhttp:$version"
        }
    }

    object ArkIvanov {

        object MVIKotlin {
            private const val version = "3.2.1"
            const val common = "com.arkivanov.mvikotlin:mvikotlin:$version"
            const val main = "com.arkivanov.mvikotlin:mvikotlin-main:$version"
            const val coroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$version"
        }

        object Decompose {
            private const val version = "1.0.0"
            const val common = "com.arkivanov.decompose:decompose:$version"
            const val jvm = "com.arkivanov.decompose:decompose-jvm:$version"
            const val compose = "com.arkivanov.decompose:extensions-compose-jetbrains:$version"
        }

        object Essenty {
            private const val version = "1.1.0"
            const val instanceKeeper = "com.arkivanov.essenty:instance-keeper:$version"
            const val parcelable = "com.arkivanov.essenty:parcelable:$version"
        }
    }

    object SqlDelight {
        private const val version = "1.5.5"
        const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$version"
        const val coroutines = "com.squareup.sqldelight:coroutines-extensions:$version"

        object Driver {
            const val android = "com.squareup.sqldelight:android-driver:$version"
            const val sqlite = "com.squareup.sqldelight:sqlite-driver:$version"
        }
    }

    object Kodein {
        private const val version = "7.20.1"
        const val common = "org.kodein.di:kodein-di:$version"
        const val compose = "org.kodein.di:kodein-di-framework-compose:$version"
        const val android = "org.kodein.di:kodein-di-framework-android-core:$version"
    }

    object Kermit {
        private const val version = "1.2.2"
        const val common = "co.touchlab:kermit:$version"
    }

    object GradleVersions {
        const val version = "0.46.0"
        const val plugin = "com.github.ben-manes.versions"
    }

    object Krypto {
        private const val version = "4.0.0"
        const val common = "com.soywiz.korlibs.krypto:krypto:$version"
        const val android = "com.soywiz.korlibs.krypto:krypto-android:$version"
    }

    object Settings {
        private const val version = "1.0.0"
        const val common = "com.russhwolf:multiplatform-settings:$version"
        const val coroutines = "com.russhwolf:multiplatform-settings-coroutines:$version"
    }

    object AndroidX {

        const val core = "androidx.core:core-ktx:1.10.1"
        const val browser = "androidx.browser:browser:1.5.0"
        const val appCompat = "androidx.appcompat:appcompat:1.6.1"

        object Compose {
            const val version = "1.4.0"
            const val activity = "androidx.activity:activity-compose:1.7.1"
        }
    }

    object Coil {
        private const val version = "2.3.0"
        const val compose = "io.coil-kt:coil-compose:$version"
    }
}