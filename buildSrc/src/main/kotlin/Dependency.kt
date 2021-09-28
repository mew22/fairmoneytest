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

        object Fragment {
            private const val VERSION = "1.3.6"
            const val fragment = "androidx.fragment:fragment-ktx:$VERSION"
        }

        object Navigation {
            private const val VERSION = "2.3.5"
            const val navigationFragment =
                "androidx.navigation:navigation-fragment-ktx:$VERSION"
            const val navigationUi = "androidx.navigation:navigation-ui-ktx:$VERSION"
        }

        object Recyclerview {
            private const val VERSION = "1.2.0"
            const val recyclerview = "androidx.recyclerview:recyclerview:$VERSION"
        }

        object SwipeRefreshLayout {
            private const val VERSION = "1.1.0"
            const val refresh = "androidx.swiperefreshlayout:swiperefreshlayout:$VERSION"
        }

        object Paging {
            private const val VERSION = "3.0.1"
            const val common = "androidx.paging:paging-common-ktx:$VERSION"
            const val runtime = "androidx.paging:paging-runtime-ktx:$VERSION"
        }

        object Room {
            private const val VERSION = "2.3.0"
            const val room = "androidx.room:room-ktx:$VERSION"
            const val compiler = "androidx.room:room-compiler:$VERSION"
        }

        object LifeCycle {
            private const val VERSION = "2.2.0"
            private const val VERSION_RUNTIME = "2.4.0-beta01"

            // use version beta to profit of repeatOnLifecycle convenient api
            const val lifecycleExt = "androidx.lifecycle:lifecycle-extensions:$VERSION"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$$VERSION"
            const val lifecycleVm = "androidx.lifecycle:lifecycle-viewmodel-ktx:$VERSION"
            const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:$VERSION_RUNTIME"
        }

        object Splashscreen {
            private const val VERSION = "1.0.0-alpha01"
            const val splash = "androidx.core:core-splashscreen:$VERSION"
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

    object KotlinX {
        object Coroutines {
            private const val VERSION = "1.5.2"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$VERSION"
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

    object SquareUp {
        object Retrofit {
            private const val VERSION = "2.9.0"
            const val retrofit = "com.squareup.retrofit2:retrofit:$VERSION"
            const val gsonConverter =
                "com.squareup.retrofit2:converter-gson:$VERSION"
            const val mock = "com.squareup.retrofit2:retrofit-mock:$VERSION"
        }

        object Okhttp {
            private const val VERSION = "4.9.1"
            private const val MOCK_VERSION = "5.0.0-alpha.2"
            const val interceptor =
                "com.squareup.okhttp3:logging-interceptor:$VERSION"
            const val mockWebServer = "com.squareup.okhttp3:mockwebserver3-junit5:$MOCK_VERSION"
        }
    }

    object Koin {
        private const val VERSION = "3.1.2"
        const val koin = "io.insert-koin:koin-core:$VERSION"
        const val android = "io.insert-koin:koin-android:$VERSION"
    }

    object Glide {
        private const val VERSION = "4.12.0"
        const val glide = "com.github.bumptech.glide:glide:$VERSION"
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
