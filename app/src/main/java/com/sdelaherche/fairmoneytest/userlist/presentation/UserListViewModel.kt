package com.sdelaherche.fairmoneytest.userlist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sdelaherche.fairmoneytest.R
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import com.sdelaherche.fairmoneytest.common.domain.usecase.SuspendUseCase
import com.sdelaherche.fairmoneytest.common.presentation.RefreshFailure
import com.sdelaherche.fairmoneytest.common.presentation.RefreshResponseModel
import com.sdelaherche.fairmoneytest.common.presentation.RefreshSuccess
import com.sdelaherche.fairmoneytest.common.presentation.getErrorMessageFromException
import com.sdelaherche.fairmoneytest.common.util.Result
import com.sdelaherche.fairmoneytest.common.util.fold
import com.sdelaherche.fairmoneytest.userlist.domain.entity.Refreshing
import com.sdelaherche.fairmoneytest.userlist.domain.entity.RefreshingError
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserList
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserListViewModel(
    getUserListUseCase: ReactiveUseCase<Unit, UserListResponse>,
    private val refreshListUseCase: SuspendUseCase<Unit, Result<Unit>>,
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
    }.flowOn(Dispatchers.IO)

    suspend fun refresh(): RefreshResponseModel = withContext(Dispatchers.IO) {
        refreshListUseCase().fold(
            onSuccess = {
                RefreshSuccess(
                    message =
                    getApplication<Application>().getString(R.string.generic_success)
                )
            },
            onFailure = { ex ->
                RefreshFailure(
                    message = getErrorMessageFromException(
                        ex as DomainException,
                        getApplication()
                    )
                )
            }
        )
    }
}
