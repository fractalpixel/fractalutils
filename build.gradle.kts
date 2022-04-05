import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Project info
val projectVersion = "3.0.2"
val projectArtifact = "fractalutils"
val projectGroup = "org.fractalpixel"
val projectDescription = "Utility library with emphasis on game, graphics and simulation related utility functions and classes."
val projectUrl = "https://github.com/fractalpixel/fractalutils"

val jvmVersion = "11"

plugins {
    // Apply kotlin jvm Plugin to add support for Kotlin.
    kotlin("jvm") version "1.6.10"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // For building packages
    `maven-publish`
}

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmVersion
}

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    artifacts {
        archives(sourcesJar)
        archives(jar)
    }
}


// Configuration for publish releases (e.g. using jitpack.io)
// Call with gradlew publish
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = projectGroup
            artifactId = projectArtifact
            version = projectVersion

            from(components["java"])

            artifact(tasks["sourcesJar"])
        }
    }
}


