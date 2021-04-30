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

    ":data-prefs",
    ":data-local",
    ":data-remote",

    ":ui-compose",

    ":utils",
    ":sdk"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}