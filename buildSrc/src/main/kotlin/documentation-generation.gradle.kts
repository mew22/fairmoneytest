@file:Suppress("unused")

import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    org.jetbrains.dokka
}

tasks.withType<DokkaTask>().configureEach {}
