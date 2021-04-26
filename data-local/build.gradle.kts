plugins {
    id("lastik-multiplatform")
    id("com.squareup.sqldelight")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Libs.SqlDelight.coroutines)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(Libs.SqlDelight.Driver.android)
            }
        }
        named("desktopMain") {
            dependencies {
                implementation(Libs.SqlDelight.Driver.sqlite)
            }
        }
    }
}

sqldelight {
    database("LastikDatabase") {
        packageName = "ru.hotmule.lastik.data.local"
    }
}