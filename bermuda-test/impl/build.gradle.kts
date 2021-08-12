repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor(project(":bermuda-test:plugin"))
}

tasks {
    compileJava {
        options.compilerArgs.add("-Xplugin:test")
    }
}