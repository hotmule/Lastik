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
        const val buildTools = "29.0.3"
    }
}

object Module {
    const val lastSdk = ":lastSdk"
}

object Libs {

    const val gradle = "com.android.tools.build:gradle:4.2.0-alpha07"
    const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
    const val preferences = "com.russhwolf:multiplatform-settings:0.6"
    const val jUnit = "junit:junit:4.13"

    object Update {
        const val version = "0.29.0"
        const val helper = "com.github.ben-manes.versions"
    }

    object Kotlin {
        private const val version = "1.3.72"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val serialization = "org.jetbrains.kotlin:kotlin-serialization:$version"
    }

    object Ktor {
        private const val version = "1.3.2"

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

    object Napier {
        private const val version = "1.3.0"
        const val common = "com.github.aakira:napier:$version"
        const val android = "com.github.aakira:napier-android:$version"
    }

    object AndroidX {

        const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        const val jUnit = "androidx.test.ext:junit:1.1.1"
        const val jUnitRunner = "androidx.test.runner.AndroidJUnitRunner"

        object Compose {
            const val version = "0.1.0-dev15"
            const val foundation = "androidx.ui:ui-foundation:$version"
            const val material = "androidx.ui:ui-material:$version"
            const val tooling = "androidx.ui:ui-tooling:$version"
            const val layout = "androidx.ui:ui-layout:$version"
        }
    }
}

object Proguard {
    const val rules = "proguard-rules.pro"
    const val consumerRules = "consumer-rules.pro"
    const val default = "proguard-android-optimize.txt"
}