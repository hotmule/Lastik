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

    const val app = ":app"
    const val shared = ":shared"

    object Ui {
        const val auth = ":ui:auth"
        const val scrobbles = ":ui:scrobbles"
        const val profile = ":ui:profile"
    }
}

object Libs {

    const val gradle = "com.android.tools.build:gradle:4.1.0-beta01"
    const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
    const val jUnit = "junit:junit:4.13"

    object Kotlin {
        private const val version = "1.3.72"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib"
        const val stdLibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common"
        const val stdLibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object AndroidX {

        const val material = "com.google.android.material:material:1.1.0"
        const val jUnit = "androidx.test.ext:junit:1.1.1"
        const val jUnitRunner = "androidx.test.runner.AndroidJUnitRunner"

        object Compose {
            const val version = "0.1.0-dev13"
            const val foundation = "androidx.ui:ui-foundation:$version"
            const val material = "androidx.ui:ui-material:$version"
            const val tooling = "androidx.ui:ui-tooling:$version"
            const val layout = "androidx.ui:ui-layout:$version"
        }

        object Navigation {
            private const val version = "2.3.0-rc01"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
        }
    }
}

object Proguard {
    const val rules = "proguard-rules.pro"
    const val consumerRules = "consumer-rules.pro"
    const val default = "proguard-android-optimize.txt"
}