rootProject.name = "Lastik"
include(

    ":app-android",
    ":app-desktop",

    ":feature-root",
    ":feature-auth",
    ":feature-library",
    ":feature-top",
    ":feature-profile",
    ":feature-scrobbles",
    ":feature-shelf",
    ":feature-browser",
    ":feature-now-playing",
    ":feature-settings",
    ":feature-user",
    ":feature-menu",

    ":data-sdk",
    ":data-local",
    ":data-remote",

    ":ui-compose",
    ":utils"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}