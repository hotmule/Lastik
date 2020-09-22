plugins {
    id(Libs.GradleVersions.plugin) version Libs.GradleVersions.version
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
        classpath(Libs.SqlDelight.gradlePlugin)
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
