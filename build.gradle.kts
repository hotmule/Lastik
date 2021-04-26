plugins {
    id(Libs.GradleVersions.plugin) version Libs.GradleVersions.version
}

buildscript {
    repositories {
        google()
        jcenter()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
        classpath(Libs.gradle)
        classpath(Libs.Kotlin.gradlePlugin)
        classpath(Libs.Kotlin.serialization)
        classpath(Libs.SqlDelight.gradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}
