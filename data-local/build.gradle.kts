plugins {
    id("lastik.multiplatform")
    id("com.squareup.sqldelight")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.sqldelight.coroutines)
            }
        }
        named("androidMain") {
            dependencies {
                api(libs.sqldelight.driver.android)
            }
        }
        named("desktopMain") {
            dependencies {
                api(libs.sqldelight.driver.sqlite)
            }
        }
    }
}

sqldelight {
    database("LastikDatabase") {
        packageName = "ru.hotmule.lastik.data.local"
    }
}