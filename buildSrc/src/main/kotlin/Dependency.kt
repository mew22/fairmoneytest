import org.gradle.api.JavaVersion

object Dependency {

    object AndroidX {
        object AppCompat {
            private const val VERSION = "1.3.1"
            const val appCompat = "androidx.appcompat:appcompat:$VERSION"
        }

        object ConstraintLayout {
            private const val VERSION = "2.1.0"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:$VERSION"
        }

        object Core {
            private const val VERSION = "1.6.0"
            const val coreKtx = "androidx.core:core-ktx:$VERSION"
        }

        object TestExt {
            private const val VERSION = "1.1.3"
            const val junit = "androidx.test.ext:junit:$VERSION"
        }

        object Test {
            private const val VERSION = "1.4.0"
            const val runner = "androidx.test:runner:$VERSION"
            const val rules = "androidx.test:rules:$VERSION"
        }

        object TestEspresso {
            private const val VERSION = "3.4.0"
            const val espressoCore = "androidx.test.espresso:espresso-core:$VERSION"
        }
    }

    object Config {
        const val applicationId = "com.sdelaherche.fairmoneytest"
        const val compileSdk = 31
        const val minSdk = 25
        val javaVersion = JavaVersion.VERSION_1_8
        const val javaVersionInt = 8
        const val jvmTarget = "1.8"
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    object GradlePlugin {
        object Kotlin {
            private const val VERSION = "1.5.30"
            const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
        }
        object Android {
            private const val VERSION = "7.0.2"
            const val plugin = "com.android.tools.build:gradle:$VERSION"
        }
    }

    object Google {
        object AndroidMaterial {
            private const val VERSION = "1.4.0"
            const val material = "com.google.android.material:material:$VERSION"
        }
    }

    object Jacoco {
        const val TOOL_VERSION = "0.8.7"
    }

    object JUnit {
        private const val VERSION = "5.8.1"
        const val jupiter = "org.junit.jupiter:junit-jupiter:${VERSION}"
    }

    object Mockk {
        private const val VERSION = "1.12.0"
        const val mockk = "io.mockk:mockk:${VERSION}"
    }
}
