package ru.hotmule.lastik.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.hotmule.lastik.convention.extensions.configureAndroid
import ru.hotmule.lastik.convention.extensions.getVersion

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.application")
                apply("kotlin-android")
            }
            extensions.configure<ApplicationExtension> {
                buildToolsVersion = project.getVersion("build-tools")
                defaultConfig {
                    applicationId = project.getVersion("application_id")
                    versionName = project.getVersion("version-name")
                    versionCode = project.getVersion("version-code").toInt()
                    targetSdk = project.getVersion("target-sdk").toInt()
                }
                configureAndroid(this)
            }
        }
    }
}