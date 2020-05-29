import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}