plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":bermuda-core"))
    api("org.ow2.asm:asm:9.2")
    api("org.ow2.asm:asm-tree:9.2")
}