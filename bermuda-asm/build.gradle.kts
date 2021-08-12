plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":bermuda-core"))
    api("org.ow2.asm:asm:9.2")
}