package ru.hotmule.lastik.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("lastik.android.library")
                apply("kotlin-multiplatform")
            }
            extensions.configure<KotlinMultiplatformExtension> {
                android()
                jvm("desktop")
            }
        }
    }
}