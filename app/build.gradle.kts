plugins {
    `android-application`
    androidx.navigation.safeargs
}

dependencies {
    implementation(Dependency.AndroidX.Fragment.fragment)

    implementation(Dependency.AndroidX.Paging.common)
    implementation(Dependency.AndroidX.Paging.runtime)

    implementation(Dependency.AndroidX.Navigation.navigationFragment)
    implementation(Dependency.AndroidX.Navigation.navigationUi)

    implementation(Dependency.AndroidX.Recyclerview.recyclerview)
    implementation(Dependency.AndroidX.SwipeRefreshLayout.refresh)

    implementation(Dependency.AndroidX.LifeCycle.livedata)
    implementation(Dependency.AndroidX.LifeCycle.lifecycleExt)
    implementation(Dependency.AndroidX.LifeCycle.lifecycleVm)
    implementation(Dependency.AndroidX.LifeCycle.lifecycleRuntime)

    implementation(Dependency.AndroidX.Splashscreen.splash)

    implementation(Dependency.AndroidX.Room.room)
    kapt(Dependency.AndroidX.Room.compiler)

    implementation(Dependency.KotlinX.Coroutines.core)
    implementation(Dependency.KotlinX.Coroutines.android)
    testImplementation(Dependency.KotlinX.Coroutines.test)
    androidTestImplementation(Dependency.KotlinX.Coroutines.test) {
        // conflicts with mockito due to direct inclusion of byte buddy
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
    }

    implementation(Dependency.SquareUp.Retrofit.retrofit)
    implementation(Dependency.SquareUp.Retrofit.gsonConverter)
    implementation(Dependency.SquareUp.Okhttp.interceptor)
    testImplementation(Dependency.SquareUp.Okhttp.mockWebServer)

    implementation(Dependency.Koin.koin)
    implementation(Dependency.Koin.android)

    implementation(Dependency.Glide.glide)
}
