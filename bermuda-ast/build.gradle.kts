plugins {
    `java-library`
}

dependencies {
    api(project(":bermuda-core"))
}

val treesDir = buildDir.resolve("generated/sources/trees")

sourceSets["main"].java.srcDir(treesDir)

tasks {
    task("generateTrees", Copy::class) {
        dependsOn(":bermuda-ast:treegen:run")
        val treegen = project(":bermuda-ast:treegen")
        from(treegen.projectDir.resolve("trees"))
        into(treesDir)
    }

    compileJava {
        dependsOn("generateTrees")

    }
}