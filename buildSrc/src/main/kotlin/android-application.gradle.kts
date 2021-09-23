@file:Suppress("UnstableApiUsage")
plugins {
    com.android.application
    `kotlin-android`
    `kotlin-kapt`
    id("qa")
    id("documentation-generation")
}

android {
    compileSdk = Dependency.Config.compileSdk

    defaultConfig {
        versionCode = 1
        versionName = "0.0.1"
        applicationId = Dependency.Config.applicationId
        minSdk = Dependency.Config.minSdk
        targetSdk = Dependency.Config.compileSdk

        testInstrumentationRunner = Dependency.Config.testInstrumentationRunner
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Dependency.Config.javaVersion
        targetCompatibility = Dependency.Config.javaVersion
    }
    kotlinOptions {
        jvmTarget = Dependency.Config.jvmTarget
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(Dependency.AndroidX.AppCompat.appCompat)
    implementation(Dependency.AndroidX.ConstraintLayout.constraintLayout)
    implementation(Dependency.AndroidX.Core.coreKtx)
    implementation(Dependency.Google.AndroidMaterial.material)

    testImplementation(Dependency.JUnit.jupiter)
    testImplementation(Dependency.Mockk.mockk)

    androidTestImplementation(Dependency.AndroidX.Test.runner)
    androidTestImplementation(Dependency.AndroidX.Test.rules)
    androidTestImplementation(Dependency.AndroidX.TestExt.junit)
    androidTestImplementation(Dependency.AndroidX.TestEspresso.espressoCore)
}