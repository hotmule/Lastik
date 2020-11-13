object App {
    const val id = "ru.hotmule.lastfmclient"
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
        const val buildTools = "30.0.0"
    }
}

object Module {

    const val sdk = ":sdk"

    object Android {
        object Compose {
            const val utils = ":android-compose-utils"
        }
    }
}

object Libs {

    const val gradle = "com.android.tools.build:gradle:4.2.0-alpha16"
    const val settings = "com.russhwolf:multiplatform-settings:0.6.2"
    const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
    const val jUnit = "junit:junit:4.13"

    object Kotlin {
        const val version = "1.4.10"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val serialization = "org.jetbrains.kotlin:kotlin-serialization:$version"
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
        const val version = "0.36.0"
        const val plugin = "com.github.ben-manes.versions"
    }

    object Krypto {
        private const val version = "1.12.0"
        const val common = "com.soywiz.korlibs.krypto:krypto:$version"
        const val android = "com.soywiz.korlibs.krypto:krypto-android:$version"
    }

    object AndroidX {

        const val fragment = "androidx.fragment:fragment-ktx:1.2.5"
        const val appCompat = "androidx.appcompat:appcompat:1.3.0-alpha02"
        const val jUnit = "androidx.test.ext:junit:1.1.1"
        const val jUnitRunner = "androidx.test.runner.AndroidJUnitRunner"

        object Compose {
            const val version = "1.0.0-alpha07"
            const val ui = "androidx.compose.ui:ui:$version"
            const val tooling = "androidx.ui:ui-tooling:$version"
            const val material = "androidx.compose.material:material:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val icons = "androidx.compose.material:material-icons-extended:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
        }
    }

    object Accompanist {
        private const val version = "0.3.3.1"
        const val coil = "dev.chrisbanes.accompanist:accompanist-coil:$version"
        const val insets = "dev.chrisbanes.accompanist:accompanist-insets:$version"
    }
}

object Proguard {
    const val rules = "proguard-rules.pro"
    const val consumerRules = "consumer-rules.pro"
    const val default = "proguard-android-optimize.txt"
}