@file:JvmName("UserListModule")

package com.sdelaherche.fairmoneytest.userlist.di

import com.sdelaherche.fairmoneytest.userlist.data.remote.IUserRemoteService
import com.sdelaherche.fairmoneytest.userlist.data.repository.UserRepository
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository
import com.sdelaherche.fairmoneytest.userlist.domain.usecase.GetUserListUseCase
import com.sdelaherche.fairmoneytest.userlist.domain.usecase.RefreshUserListUseCase
import com.sdelaherche.fairmoneytest.userlist.presentation.UserListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val userListModule = module {
    single { get<Retrofit>().create(IUserRemoteService::class.java) }

    single<IUserRepository> {
        UserRepository(
            remoteSource = get(),
            localSource = get()
        )
    }

    factory {
        GetUserListUseCase(userRepository = get())
    }

    factory {
        RefreshUserListUseCase(userRepository = get())
    }

    viewModel {
        UserListViewModel(
            getUserListUseCase = get<GetUserListUseCase>(),
            refreshListUseCase = get<RefreshUserListUseCase>(),
            application = androidApplication()
        )
    }
}
