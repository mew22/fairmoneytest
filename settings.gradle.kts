dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    buildscript {
        repositories {
            google()
            mavenCentral()
            maven {
                url = uri("https://plugins.gradle.org/m2/")
            }
        }
    }
}

rootProject.name = "fairmoneytest"

include(":app")