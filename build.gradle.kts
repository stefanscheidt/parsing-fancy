import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.h0tk3y.betterParse:better-parse:0.4.4")

    // https://junit.org/junit5/docs/current/user-guide/
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://kotlinlang.org/api/latest/kotlin.test/kotlin.test/
    testImplementation(kotlin("test"))

    // https://kotest.io/docs/quickstart
    testImplementation("io.kotest:kotest-assertions-core:5.5.4")

    // https://mockk.io/
    testImplementation("io.mockk:mockk:1.13.2")
}
