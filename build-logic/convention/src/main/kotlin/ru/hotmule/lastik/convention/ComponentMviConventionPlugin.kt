package ru.hotmule.lastik.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.hotmule.lastik.convention.extensions.libs

class ComponentMviConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("lastik.component")
            }
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    named("commonMain") {
                        dependencies {
                            implementation(project(":utils"))
                            implementation(libs.findLibrary("mvikotlin").get())
                            implementation(libs.findLibrary("mvikotlin-coroutines").get())
                            implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                        }
                    }
                }
            }
        }
    }
}