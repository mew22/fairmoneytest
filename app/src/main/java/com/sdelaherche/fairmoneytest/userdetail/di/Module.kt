@file:JvmName("UserDetailModule")

package com.sdelaherche.fairmoneytest.userdetail.di

import com.sdelaherche.fairmoneytest.userdetail.data.remote.IUserDetailRemoteService
import com.sdelaherche.fairmoneytest.userdetail.data.repository.UserDetailRepository
import com.sdelaherche.fairmoneytest.userdetail.domain.repository.IUserDetailRepository
import com.sdelaherche.fairmoneytest.userdetail.domain.usecase.GetUserDetailUseCase
import com.sdelaherche.fairmoneytest.userdetail.domain.usecase.RefreshUserDetailUseCase
import com.sdelaherche.fairmoneytest.userdetail.presentation.UserDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val userDetailModule = module {
    single { get<Retrofit>().create(IUserDetailRemoteService::class.java) }

    single<IUserDetailRepository> {
        UserDetailRepository(
            remoteSource = get(),
            localSource = get()
        )
    }

    factory {
        GetUserDetailUseCase(userDetailRepository = get())
    }

    factory {
        RefreshUserDetailUseCase(userDetailRepository = get())
    }

    viewModel { parameters ->
        UserDetailViewModel(
            id = parameters.get(),
            getUserUseCase = get<GetUserDetailUseCase>(),
            refreshUseCase = get<RefreshUserDetailUseCase>()
        )
    }
}
