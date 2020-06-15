import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.saveToFile
import java.util.*

plugins {
    kotlin("jvm").version("1.3.72")
}

group = "com.davioooh.lptools"
version = "0.1.0-SNAPSHOT"
description = "leanpub-manuscript-tools"

repositories {
    mavenLocal()
    mavenCentral()
}

val junitVersion = "5.6.2"
val assertjVersion = "3.16.1"
val cliktVersion = "2.7.1"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.ajalt:clikt-multiplatform:$cliktVersion")
    testImplementation(kotlin("test-junit"))
    testImplementation(junit5("api"))
    testImplementation(junit5("params"))
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testRuntimeOnly(junit5("engine"))
}

fun DependencyHandler.junit5(module: String, version: String? = junitVersion): Any =
        "org.junit.jupiter:junit-jupiter-$module${version?.let { ":$version" } ?: ""}"

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.register("createVersionProps") {
    dependsOn("processResources")
    doLast {
        val p = Properties()
        p["version"] = project.version.toString()
        p.saveToFile(org.jetbrains.kotlin.konan.file.File("$buildDir/resources/main/version.properties"))
    }
}

tasks.classes {
    dependsOn("createVersionProps")
}