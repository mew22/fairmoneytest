package com.sdelaherche.fairmoneytest.userdetail.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sdelaherche.fairmoneytest.R
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.UserDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.sdelaherche.fairmoneytest.userdetail.domain.Detail
import com.sdelaherche.fairmoneytest.userdetail.domain.Refreshing
import com.sdelaherche.fairmoneytest.userdetail.domain.RefreshingError
import com.sdelaherche.fairmoneytest.userdetail.domain.UserDetailResponse

class UserDetailViewModel(
    private val getUserUseCase: ReactiveUseCase<Id, UserDetailResponse>,
    private val refreshUseCase: ReactiveUseCase<Id, Result<Boolean>>,
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

    fun refresh(): Flow<Result<Boolean>> = refreshUseCase(Id(id))
}
