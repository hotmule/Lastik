import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    id("lastik-multiplatform")
}

kotlin {

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Libs.Kodein.common)
                api(Libs.ArkIvanov.Essenty.lifecycle)
                api(Libs.ArkIvanov.Decompose.common)
            }
        }
    }
}
