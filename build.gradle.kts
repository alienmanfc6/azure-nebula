buildscript {

    // dependency versions
    extra.apply {
        set("gradleVersion", "7.4.2")
        set("kotlinVersion", "1.9.10")
        set("coroutinesVersion", "1.6.4")
        set("serializationVersion", "1.4.1")
    }

    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kotlinVersion"] as String}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${project.extra["kotlinVersion"] as String}")
        classpath("com.android.tools.build:gradle:${project.extra["gradleVersion"] as String}")
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

plugins {
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}