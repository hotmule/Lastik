buildscript {
    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath(Deps.gradle)
        classpath(Deps.Kotlin.gradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}
