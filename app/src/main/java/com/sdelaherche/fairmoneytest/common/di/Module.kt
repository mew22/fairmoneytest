@file:JvmName("CommonModule")
package com.sdelaherche.fairmoneytest.common.di

import androidx.room.Room
import com.sdelaherche.fairmoneytest.common.data.database.UserDataBase
import com.sdelaherche.fairmoneytest.common.data.remote.Config
import com.sdelaherche.fairmoneytest.common.data.remote.ErrorsCallAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val commonModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            UserDataBase::class.java,
            UserDataBase.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }

    single { get<UserDataBase>().userDao }

    single {
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addCallAdapterFactory(ErrorsCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}