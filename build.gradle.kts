// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()

    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Version.gradlePlugin}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:${Version.mavenPlugin}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        flatDir { dirs = setOf(
            File("libs"),
            project(":playablerecyclerview").file("libs")
        ) }
    }
}

tasks {
    val clean by registering(Delete::class){
        delete(buildDir)
    }
}
