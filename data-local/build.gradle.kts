plugins {
    id("lastik.multiplatform")
    id("app.cash.sqldelight")
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
    databases {
        create("LastikDatabase") {
            packageName.set("ru.hotmule.lastik.data.local")
        }
    }
}