rootProject.name = "Lastik"

include(

    ":app-android",
    ":app-desktop",

    ":feature-root",
    ":feature-auth",
    ":feature-library",
    ":feature-top",
    ":feature-main",
    ":feature-scrobbles",
    ":feature-shelf",
    ":feature-browser",
    ":feature-now-playing",
    ":feature-settings",
    ":feature-profile",

    ":data-sdk",
    ":data-local",
    ":data-remote",

    ":ui-compose",
    ":utils"
)

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
