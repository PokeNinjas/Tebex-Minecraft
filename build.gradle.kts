import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("fabric-loom") version "1.5-SNAPSHOT" apply false
}

defaultTasks("shadowJar")

group = "io.tebex"
version = "3.0.0-Beta-1"

subprojects {
    plugins.apply("java")
    plugins.apply("com.github.johnrengelman.shadow")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.named("shadowJar", ShadowJar::class.java) {
        archiveFileName.set("tebex-${project.name}-${rootProject.version}.jar")
    }

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/groups/public/") {
            name = "sonatype"
        }
        maven("https://repo.opencollab.dev/main/") {
            name = "opencollab-snapshot-repo"
        }
        maven("https://repo.papermc.io/repository/maven-public/") {
            name = "papermc-repo"
        }
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
            name = "extendedclip-repo"
        }
        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            name = "sonatype-snapshots"
        }
        maven("https://maven.nucleoid.xyz/") {
            name = "nucleoid"
        }
        maven("https://repository.mythicalnetwork.live/repository/maven-releases/") {
            name = "Mythical-Repo"
            credentials {
                username = getMythicalCredentials().first
                password = getMythicalCredentials().second
            }
        }
        maven("https://repo.lightdream.dev/")
        maven("https://maven.wispforest.io")
    }

    tasks.named("processResources", Copy::class.java) {
        val props = mapOf("version" to rootProject.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        filesNotMatching("**/*.zip") {
            expand(props)
        }
    }
}

val fabricProject = project(":fabric")
fabricProject.configure<JavaPluginExtension> {
    sourceSets {
        getByName("main") {
            java {
                srcDir("src/main/kotlin")
            }
        }
    }
}

private fun getMythicalCredentials(): Pair<String?, String?> {
    val username = (project.findProperty("mythical.auth.username") ?: project.findProperty("mythicalUsername") ?: System.getenv("MYTHICAL_USERNAME") ?: "") as String?
    val password = (project.findProperty("mythical.auth.password") ?: project.findProperty("mythicalPassword") ?: System.getenv("MYTHICAL_PASSWORD") ?: "") as String?
    return Pair(username, password)
}