import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("lastik-android")
    id("kotlin-multiplatform")
}

kotlin {

    jvm("desktop")
    android()

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}