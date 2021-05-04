rootProject.name = "Lastik"
include(

    ":app-android",
    ":app-desktop",
    ":app-web",

    ":feature-root",
    ":feature-auth",
    ":feature-library",
    ":feature-top",
    ":feature-profile",
    ":feature-scrobbles",
    ":feature-shelf",

    ":data-prefs",
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