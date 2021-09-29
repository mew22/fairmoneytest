package com.sdelaherche.fairmoneytest.userlist.presentation

import androidx.lifecycle.ViewModel
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserListViewModel(
    getUserListUseCase: ReactiveUseCase<Unit, Result<List<User>>>,
    private val refreshListUseCase: ReactiveUseCase<Unit, Result<Boolean>>
) :
    ViewModel() {

    val userList: Flow<Result<List<UserModel>>> = getUserListUseCase().map { result ->
        result.map {
            it.toUi()
        }
    }

    fun refresh(): Flow<Result<Boolean>> = refreshListUseCase()
}
