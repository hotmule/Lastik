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
                api(Libs.SqlDelight.Driver.android)
            }
        }
        named("desktopMain") {
            dependencies {
                api(Libs.SqlDelight.Driver.sqlite)
            }
        }
    }
}

sqldelight {
    database("LastikDatabase") {
        packageName = "ru.hotmule.lastik.data.local"
    }
}