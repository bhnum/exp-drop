import io.papermc.hangarpublishplugin.model.Platforms

plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.hangar-publish-plugin") version "0.0.5"
}

group = "com.github.devcyntrix"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://maven.enginehub.org/repo/")

    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://raw.githubusercontent.com/FabioZumbi12/RedProtect/mvn-repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repository.minecodes.pl/releases")

    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.google.inject:guice:7.0.0")
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-platform-bukkit:4.3.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.14.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.14.0")

    implementation("org.bstats:bstats-bukkit:3.0.2")

    compileOnly("org.jetbrains:annotations:23.0.0")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        filesMatching("plugin.yml") {
            expand(Pair("projectVersion", project.version))
        }
    }
    runServer {
        minecraftVersion("1.20.2")
    }
    test {
        useJUnitPlatform()
    }
    shadowJar {
        relocate("org.bstats", "com.github.devcyntrix.expdrop.metrics")
    }
}

hangarPublish {
    publications.register("ExpDrop") {
        version.set(project.version as String)
        namespace("CyntrixAlgorithm", "ExpDrop")
        changelog.set("https://github.com/DevCyntrix/expdrop/blob/main/CHANGELOG")

        apiKey.set(System.getenv("API_KEY"))

        if (!project.version.toString().contains('-')) {
            channel.set("Release")
        } else {
            channel.set("Snapshot")
        }

        platforms {
            register(Platforms.PAPER) {
                jar.set(tasks.shadowJar.flatMap { it.archiveFile })
                platformVersions.set(listOf("1.17-1.20.2"))
            }
        }

    }
}