plugins {
    id("lastik-multiplatform")
    id("com.squareup.sqldelight")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Libs.Kodein.common)
                implementation(Libs.SqlDelight.coroutines)
            }
        }
        named("androidMain") {
            dependencies {
                api(Libs.SqlDelight.Driver.android)
            }
        }
        named("desktopMain") {
            dependencies {
                api(Libs.SqlDelight.Driver.sqlite)
            }
        }
        named("iosSimulatorArm64Main") {
            dependencies {
                api(Libs.SqlDelight.Driver.native)
            }
        }
    }
}

sqldelight {
    database("LastikDatabase") {
        packageName = "ru.hotmule.lastik.data.local"
    }
}
