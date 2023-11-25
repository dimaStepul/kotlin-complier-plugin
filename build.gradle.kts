import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    application
}

allprojects {
    group = "org.stepik"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("17"))
    }
}


dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.0")
    testImplementation(kotlin("test"))
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.5.0")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

//tasks.withType<KotlinCompile> {
//    compilerOptions {
//        freeCompilerArgs.add("-Xplugin=${project.rootDir}/build/libs/comp_plugin-1.0-SNAPSHOT.jar")
//    }
//}