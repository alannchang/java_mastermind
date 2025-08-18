plugins {
    id("java")
    id("application")
}

group = "org.alanc.mastermind"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // slf4j
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // SQLite
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")

    // JUnit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.7.0")

    // MockWebServer
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }
}

tasks.named<JavaExec>("run") {
    mainClass.set("org.alanc.mastermind.Main")
    standardInput = System.`in`
}