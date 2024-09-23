package ru.hotmule.lastik.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.hotmule.lastik.convention.extensions.configureAndroid

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
            }
            extensions.configure<LibraryExtension> {
                configureAndroid(this)
            }
        }
    }
}