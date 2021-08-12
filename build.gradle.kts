plugins {
    idea
    id("org.transparent.diamond") version "1.1.0" apply false
}

allprojects {
    apply(plugin = "org.transparent.diamond")
    apply(plugin = "java")

    group = "xyz.maow"
    version = "1.0.0"
}


repositories {
    mavenCentral()
}

dependencies {

}