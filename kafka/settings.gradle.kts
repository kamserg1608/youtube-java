rootProject.name = "kafka"

pluginManagement {
    val jgitver: String by settings
    val spotless: String by settings
    val springframeworkBoot: String by settings
    val dependencyManagement: String by settings
    val lombok: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("com.diffplug.spotless") version spotless

        id("org.springframework.boot") version springframeworkBoot
        id("io.freefair.lombok") version lombok
    }
}
include("producer")
include("first_consumer")
include("second_consumer")