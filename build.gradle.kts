buildscript {
    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath(Deps.gradle)
        classpath(Deps.kotlinGradle)
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
