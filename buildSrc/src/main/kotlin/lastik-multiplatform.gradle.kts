import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("lastik-android")
    id("kotlin-multiplatform")
}

configurations {
    create("androidTestApi")
    create("androidTestDebugApi")
    create("androidTestReleaseApi")
    create("testApi")
    create("testDebugApi")
    create("testReleaseApi")
}

kotlin {

    jvm("desktop")
    android()

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}