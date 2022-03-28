import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

// Project info
val projectGroup = "fractalutils"
val projectArtifact = "fractalutils"
val projectVersion = "3.0.0-rc.2"


plugins {
    // Apply kotlin jvm Plugin to add support for Kotlin.
    kotlin("jvm") version "1.6.10"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // For publishing the maven-style package for this library
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



// Publish releases to GitHub Packages
// Call with gradlew publish, from a github activity.
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = projectGroup
            artifactId = projectArtifact
            version = projectVersion

            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/fractalpixel/fractalutils")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

