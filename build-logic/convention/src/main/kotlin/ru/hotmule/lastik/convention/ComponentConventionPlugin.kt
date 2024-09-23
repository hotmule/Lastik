package ru.hotmule.lastik.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.hotmule.lastik.convention.extensions.libs

class ComponentConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("lastik.multiplatform")
            }
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    named("commonMain") {
                        dependencies {
                            implementation(libs.findLibrary("kodein").get())
                            implementation(libs.findLibrary("decompose").get())
                        }
                    }
                }
            }
        }
    }
}