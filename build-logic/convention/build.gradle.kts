import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "ru.hotmule.lastik.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.gradle)
    compileOnly(libs.kotlin.gradle)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "lastik.android.application"
            implementationClass = "ru.hotmule.lastik.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "lastik.android.library"
            implementationClass = "ru.hotmule.lastik.convention.AndroidLibraryConventionPlugin"
        }
        register("multiplatform") {
            id = "lastik.multiplatform"
            implementationClass = "ru.hotmule.lastik.convention.MultiplatformConventionPlugin"
        }
        register("component") {
            id = "lastik.component"
            implementationClass = "ru.hotmule.lastik.convention.ComponentConventionPlugin"
        }
        register("componentMvi") {
            id = "lastik.component.mvi"
            implementationClass = "ru.hotmule.lastik.convention.ComponentMviConventionPlugin"
        }
    }
}