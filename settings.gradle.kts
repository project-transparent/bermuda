pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "bermuda"
include("bermuda-core")
include("bermuda-ast")
include("bermuda-asm")
include("bermuda-test")
include("bermuda-test:plugin")
findProject(":bermuda-test:plugin")?.name = "plugin"
include("bermuda-test:impl")
findProject(":bermuda-test:impl")?.name = "impl"