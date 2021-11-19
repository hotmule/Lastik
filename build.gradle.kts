plugins {
    id(Libs.GradleVersions.plugin) version Libs.GradleVersions.version
}

buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
    dependencies {
        classpath(Libs.gradle)
        classpath(Libs.Kotlin.gradlePlugin)
        classpath(Libs.Kotlin.Serialization.common)
        classpath(Libs.SqlDelight.gradlePlugin)
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}
