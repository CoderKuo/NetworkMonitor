import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.izzel.taboolib.gradle.*


plugins {
    java
    id("io.izzel.taboolib") version "2.0.20"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

taboolib {
    env {
        install(Basic)
        install(Bukkit)
        install(BukkitUtil)
        install(BukkitNMS)
        install(BukkitNMSUtil)
        install(MinecraftChat)
        install(I18n)
        install(Metrics)
        install(CommandHelper)
    }
    description {
        name = "NetworkMonitor"
    }
    version { taboolib = "6.2.0" }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v12002:12002-minimize:mapped")
    compileOnly("ink.ptms.core:v12002:12002-minimize:universal")
    compileOnly("ink.ptms.core:v12100:12100-minimize:mapped")
    compileOnly("ink.ptms.core:v12100:12100-minimize:universal")
    compileOnly("ink.ptms.core:v11801:11801:mapped")
    compileOnly("ink.ptms.core:v11801:11801:universal")
    compileOnly("ink.ptms.core:v11200:11200")
    compileOnly("ink.ptms.core:v11605:11605")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))

    compileOnly("io.netty:netty-all:4.1.115.Final")

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
