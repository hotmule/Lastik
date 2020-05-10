object App {
    const val id = "ru.hotmule.multitodo"
}

object Module {
    const val shared = ":shared"
}

object AppVersion {
    const val code = 1
    const val name = "1.0"
}

object SdkVersion {
    const val min = 21
    const val target = 29
    const val compile = 29
    const val buildTools = "29.0.3"
}

object Deps {

    //gradle
    const val gradle = "com.android.tools.build:gradle:${DepVersion.gradle}"

    //kotlin
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib"
    const val kotlinCommon = "org.jetbrains.kotlin:kotlin-stdlib-common"
    const val kotlinJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${DepVersion.kotlin}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${DepVersion.kotlin}"

    //androidX
    const val core = "androidx.core:core-ktx:${DepVersion.core}"
    const val appCompat = "androidx.appcompat:appcompat:${DepVersion.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${DepVersion.constraintLayout}"

    //testing
    const val jUnit = "junit:junit:${DepVersion.jUnit}"
    const val androidJUnit = "androidx.test.ext:junit:${DepVersion.androidJUnit}"
    const val espresso = "androidx.test.espresso:espresso-core:${DepVersion.espresso}"
    const val androidJUnitRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object DepVersion {

    const val gradle = "3.6.3"

    const val kotlin = "1.3.72"

    const val core = "1.2.0"
    const val appCompat = "1.1.0"
    const val constraintLayout = "1.1.3"

    const val jUnit = "4.13"
    const val androidJUnit = "1.1.1"
    const val espresso = "3.2.0"
}

object Proguard {
    const val rules = "proguard-rules.pro"
    const val consumerRules = "consumer-rules.pro"
    const val default = "proguard-android-optimize.txt"
}