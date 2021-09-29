package com.sdelaherche.fairmoneytest

import android.app.Application
import com.sdelaherche.fairmoneytest.common.di.commonModule
import com.sdelaherche.fairmoneytest.userdetail.di.userDetailModule
import com.sdelaherche.fairmoneytest.userlist.di.userListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FairMoneyTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FairMoneyTestApplication)
            modules(modules = listOf(commonModule, userListModule, userDetailModule))
        }
    }
}
