plugins {
    id(Libs.Update.helper) version Libs.Update.version
}

buildscript {
    repositories {
        google()
        jcenter()

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
    }
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}
