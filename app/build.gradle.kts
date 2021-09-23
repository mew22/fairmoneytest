plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = Dependency.Config.compileSdk

    defaultConfig {
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
}

dependencies {

    implementation(Dependency.AndroidX.AppCompat.appCompat)
    implementation(Dependency.AndroidX.ConstraintLayout.constraintLayout)
    implementation(Dependency.AndroidX.Core.coreKtx)
    implementation(Dependency.Google.AndroidMaterial.material)

    testImplementation(Dependency.JUnit.junitApi)
    testImplementation(Dependency.Mockk.mockk)

    androidTestImplementation(Dependency.AndroidX.TestExt.junit)
    androidTestImplementation(Dependency.AndroidX.TestEspresso.espressoCore)
}