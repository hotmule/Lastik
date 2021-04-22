rootProject.name = "Lastik"
include(

    ":app-android",
    ":app-desktop",

    ":feature-root",
    ":feature-auth",
    ":feature-library",
    ":feature-shelf",

    ":data-remote",
    ":data-prefs",

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
