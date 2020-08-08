plugins {
    id(Libs.Update.helper) version Libs.Update.version
}

buildscript {

    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
    dependencies {
        classpath(Libs.gradle)
        classpath(Libs.Kotlin.gradlePlugin)
        classpath(Libs.Kotlin.serialization)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap/")
    }
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}
