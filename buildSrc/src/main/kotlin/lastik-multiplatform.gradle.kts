plugins {
    kotlin("multiplatform")
    id("lastik-android")
}

kotlin {

    android()
    jvm("desktop")
    ios()
}