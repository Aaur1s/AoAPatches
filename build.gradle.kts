import net.darkhax.curseforgegradle.TaskPublishCurseForge

plugins {
    idea
    `maven-publish`
    kotlin("jvm") version "2.2.10"
    id("net.minecraftforge.gradle") version "6+"
    id("org.spongepowered.mixin") version "0.7.38"
    id("com.modrinth.minotaur") version "2.+"
    id("net.darkhax.curseforgegradle") version "1.+"
}

val minecraftVersion: String by ext
val forgeVersion: String by ext
val modId: String by ext
val modName: String by ext

group = properties["group"].toString()
version = properties["version"].toString()

java {
    withSourcesJar()
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
    add(sourceSets.main.get(), "aoapatches.mixins.refmap.json")
    config("aoapatches.mixins.json")
}

minecraft {
    mappings("official", minecraftVersion)

    runs {
        create("client") {
            workingDirectory(project.file("run/client"))

            property("forge.logging.console.level", "debug")

            property("mixin.env.remapRefMap", "true")

            mods {
                create(modId) {
                    source(sourceSets["main"])
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run/server"))

            property("forge.logging.console.level", "debug")

            property("mixin.env.remapRefMap", "true")

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
    implementation(fg.deobf("maven.modrinth:quark:r2.4-322"))
    implementation(fg.deobf("maven.modrinth:autoreglib:1.6-49"))
    implementation(fg.deobf("curse.maven:pams-harvestcraft-2-food-core-372534:3190867"))
    implementation(fg.deobf("curse.maven:pams-harvestcraft-2-crops-361385:3276350"))
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks {
    withType<ProcessResources> {
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

    afterEvaluate {
        named("addMixinsToJar") {
            setDependsOn(listOf("compileJava", "compileKotlin"))
        }
    }

    register<TaskPublishCurseForge>("curseforge") {
        group = "publishing"
        apiToken = project.properties["curseforge.token"].toString()

        upload(1330425, jar) {
            releaseType = "release"
            addGameVersion(minecraftVersion)
            addModLoader("forge")
            addRequirement("advent-of-ascension-nevermine")
        }
    }
}

modrinth {
    token = project.properties["modrinth.token"].toString()
    projectId = "aoa-patches"
    versionNumber = "$minecraftVersion-$version"
    versionType = "release"
    uploadFile.set(tasks.jar)
    gameVersions.add(minecraftVersion)
    loaders.add("forge")
    dependencies {
        required.version("adventofascension", "1.16.5-3.6.11")
    }
}