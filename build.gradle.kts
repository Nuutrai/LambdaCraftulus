plugins {
    kotlin("jvm") version "2.1.10"
}

group = "me.chriss99"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.minestom:minestom-snapshots:7589b3b655")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}