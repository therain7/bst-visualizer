rootProject.name = "trees"
include("lib")
include("app")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    versionCatalogs {
        create("libs") {
            version("kotlin", "1.9.22")
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
            plugin("kotlin-noarg", "org.jetbrains.kotlin.plugin.noarg").versionRef("kotlin")

            plugin("compose", "org.jetbrains.compose").version("1.6.0")

            library("koin-core", "io.insert-koin:koin-core:3.5.3")

            library("kotlinx-serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

            version("exposed", "0.48.0")
            library("exposed-core", "org.jetbrains.exposed", "exposed-core").versionRef("exposed")
            library("exposed-dao", "org.jetbrains.exposed", "exposed-dao").versionRef("exposed")
            library("exposed-jdbc", "org.jetbrains.exposed", "exposed-jdbc").versionRef("exposed")

            library("postgresql", "org.postgresql:postgresql:42.7.2")

            version("neo4j-ogm", "4.0.9")
            library("neo4j-ogm-core", "org.neo4j", "neo4j-ogm-core").versionRef("neo4j-ogm")
            library("neo4j-ogm-bolt", "org.neo4j", "neo4j-ogm-bolt-driver").versionRef("neo4j-ogm")

            library("junit-jupiter", "org.junit.jupiter:junit-jupiter:5.10.2")
        }
    }
}
