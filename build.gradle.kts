plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "dev.vanutp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jline:jline:3.25.1")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass = "dev.vanutp.tower_of_hanoi.MainKt"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
