rootProject.name = "Lastik"
include(
    ":android-app",
    ":sdk",

    ":feature-root",
    ":feature-auth",
    ":feature-main",

    ":ui-compose"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
