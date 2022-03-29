import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Project info
val projectGroup = "fractalutils"
val projectArtifact = "fractalutils"
val projectVersion = "3.0.0-rc.6"


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
    kotlinOptions.jvmTarget = "11"
}

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    /*
    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }
    */

    artifacts {
        archives(sourcesJar)
        // archives(javadocJar)
        archives(jar)
    }
}
