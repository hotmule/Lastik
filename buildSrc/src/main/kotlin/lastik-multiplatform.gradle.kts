import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    kotlin("multiplatform")
    id("lastik-android")
}

kotlin {

    android()
    jvm("desktop")
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "FeatureRoot"
            linkerOpts.add("-lsqlite3")
            export(Libs.ArkIvanov.Decompose.common)
            export(Libs.ArkIvanov.Essenty.lifecycle)
        }
    }
}
