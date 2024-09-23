plugins {
    id("lastik.android.application")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    
    implementation(projects.uiCompose)
    implementation(projects.featureRoot)
    implementation(projects.featureNowPlaying)

    implementation(compose.runtime)
    implementation(libs.kodein.android.core)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)

    implementation(libs.decompose)
}