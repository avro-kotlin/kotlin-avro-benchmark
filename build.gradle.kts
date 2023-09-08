import kotlinx.benchmark.gradle.JvmBenchmarkTarget

plugins {
    java
    kotlin("jvm") version "1.9.10"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.9"
    kotlin("plugin.allopen") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"

}

group = "com.github.avrokotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.4")
    implementation("com.github.avro-kotlin.avro4k:avro4k-core:1.9.0-LOCAL")
    
    //Jackson
    val jacksonVersion = "2.15.2"
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-avro:$jacksonVersion")

    testImplementation(kotlin("test"))
}
allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}
benchmark {
    configurations {
        named("main") {
            iterationTime = 5
            iterationTimeUnit = "sec"

        }
    }
    targets {
        register("main") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.21"
        }
    }
}
tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}