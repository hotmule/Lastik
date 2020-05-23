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
    const val shared = ":shared"
}

object Deps {

    const val gradle = "com.android.tools.build:gradle:3.6.3"

    object Kotlin {
        private const val version = "1.3.72"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib"
        const val stdLibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common"
        const val stdLibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${version}"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${version}"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.2.0"
        const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    }

    object Testing {
        const val jUnit = "junit:junit:4.13"
        const val androidJUnit = "androidx.test.ext:junit:1.1.1"
        const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
        const val androidJUnitRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

object Proguard {
    const val rules = "proguard-rules.pro"
    const val consumerRules = "consumer-rules.pro"
    const val default = "proguard-android-optimize.txt"
}