plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    google()
    mavenCentral()
}

dependencies {

    // Kotlin
    val kotlin = "1.5.30"
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")

    // Gradle
    val gradle = "7.0.2"
    implementation("com.android.tools.build:gradle:$gradle")

    // Navigation safeArgs
    val safeArgs = "2.3.5"
    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:$safeArgs")

    // KtLint
    val ktLintPluginVersion = "10.2.0"
    implementation("org.jlleitschuh.gradle:ktlint-gradle:${ktLintPluginVersion}")

    // Detekt
    val detektPluginVersion = "1.18.1"
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${detektPluginVersion}")

    // Dokka
    val dokkaPluginVersion = "1.5.30"
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:${dokkaPluginVersion}")

    // Jacoco
    val jacocoPluginVersion = "0.8.7"
    implementation("org.jacoco:org.jacoco.core:${jacocoPluginVersion}")
}
