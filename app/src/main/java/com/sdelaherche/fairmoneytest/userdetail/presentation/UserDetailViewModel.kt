package com.sdelaherche.fairmoneytest.userdetail.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sdelaherche.fairmoneytest.R
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import com.sdelaherche.fairmoneytest.common.domain.usecase.SuspendUseCase
import com.sdelaherche.fairmoneytest.common.presentation.RefreshFailure
import com.sdelaherche.fairmoneytest.common.presentation.RefreshResponseModel
import com.sdelaherche.fairmoneytest.common.presentation.RefreshSuccess
import com.sdelaherche.fairmoneytest.common.presentation.getErrorMessageFromException
import com.sdelaherche.fairmoneytest.userdetail.domain.Detail
import com.sdelaherche.fairmoneytest.userdetail.domain.Refreshing
import com.sdelaherche.fairmoneytest.userdetail.domain.RefreshingError
import com.sdelaherche.fairmoneytest.userdetail.domain.UserDetailResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDetailViewModel(
    private val getUserUseCase: ReactiveUseCase<Id, UserDetailResponse>,
    private val refreshUseCase: SuspendUseCase<Id, Result<Unit>>,
    private val id: String,
    application: Application
) :
    AndroidViewModel(application) {

    val userDetail: Flow<UserDetailResponseModel> =
        getUserUseCase(Id(id)).map { result ->
            when (result) {
                is Refreshing -> {
                    Loading
                }
                is RefreshingError -> {
                    Failure(
                        message = getErrorMessageFromException(result.ex, getApplication())
                    )
                }
                is Detail -> {
                    Success(detail = result.detail.toUi())
                }
            }
        }

    suspend fun refresh(): RefreshResponseModel = refreshUseCase(Id(id)).fold(
        onSuccess = { RefreshSuccess(message = getApplication<Application>().getString(R.string.generic_success)) },
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
