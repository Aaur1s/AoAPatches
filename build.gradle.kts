plugins {
    idea
    `maven-publish`
    kotlin("jvm") version "2.2.10"
    id("net.minecraftforge.gradle") version "6+"
    id("org.spongepowered.mixin") version "0.7.38"
}

val minecraftVersion: String by ext
val forgeVersion: String by ext
val modId: String by ext
val modName: String by ext

group = properties["group"].toString()
version = properties["version"].toString()

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    base.archivesName = modName
}

kotlin {
    jvmToolchain(8)

    sourceSets.all {
        languageSettings {
            enableLanguageFeature("ContextParameters")
        }
    }
}

mixin {
    config("aoapatches.mixins.json")
}

minecraft {
    mappings("official", minecraftVersion)

    runs {
        create("client") {
            workingDirectory(project.file("run/client"))

            property("forge.logging.console.level", "debug")

            mods {
                create(modId) {
                    source(sourceSets["main"])
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run/server"))
            mods {
                create(modId) {
                    source(sourceSets["main"])
                }
            }
        }
    }
}

repositories {
    mavenCentral()
    maven("https://files.minecraftforge.net/maven/")
    maven("https://api.modrinth.com/maven")
    maven("https://cursemaven.com")
}

dependencies {
    minecraft("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")
    implementation(fg.deobf("maven.modrinth:adventofascension:1.16.5-3.6.11"))
    implementation(fg.deobf("maven.modrinth:enablemultiplayermode:1.0.0+Forge1.16.X"))
    implementation(fg.deobf("curse.maven:pams-harvestcraft-2-food-core-372534:3190867"))
    implementation(fg.deobf("curse.maven:pams-harvestcraft-2-crops-361385:3276350"))
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks.withType<ProcessResources> {
    val remap = mapOf(
        "version" to project.version,
        "modId" to modId,
        "modName" to modName,
    )

    inputs.properties(remap)

    filesMatching(setOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(remap)
    }
}