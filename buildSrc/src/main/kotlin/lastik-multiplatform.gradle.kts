import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("lastik-android")
}

configurations {
    create("testApi")
    create("testDebugApi")
    create("testReleaseApi")
    create("androidTestApi")
    create("androidTestDebugApi")
    create("androidTestReleaseApi")
}

kotlin {

    android()
    jvm("desktop")

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}