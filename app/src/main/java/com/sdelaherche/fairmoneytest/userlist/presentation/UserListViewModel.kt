package com.sdelaherche.fairmoneytest.userlist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sdelaherche.fairmoneytest.R
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.sdelaherche.fairmoneytest.userlist.domain.entity.Refreshing
import com.sdelaherche.fairmoneytest.common.presentation.getErrorMessageFromException
import com.sdelaherche.fairmoneytest.userlist.domain.entity.RefreshingError
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserList
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserListResponse

class UserListViewModel(
    getUserListUseCase: ReactiveUseCase<Unit, UserListResponse>,
    private val refreshListUseCase: ReactiveUseCase<Unit, Result<Boolean>>,
    application: Application
) :
    AndroidViewModel(application) {

    val userList: Flow<UserListResponseModel> = getUserListUseCase().map { result ->
        when (result) {
            is Refreshing -> {
                Loading
            }
            is RefreshingError -> {
                Failure(message = getErrorMessageFromException(result.ex, getApplication()))
            }
            is UserList -> {
                Success(list = result.list.toUi())
            }
        }
    }

    fun refresh(): Flow<Result<Boolean>> = refreshListUseCase()
}
