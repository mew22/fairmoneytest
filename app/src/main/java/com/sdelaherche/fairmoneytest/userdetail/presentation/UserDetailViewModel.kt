package com.sdelaherche.fairmoneytest.userdetail.presentation

import androidx.lifecycle.ViewModel
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.UserDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDetailViewModel(
    private val getUserUseCase: ReactiveUseCase<Id, Result<UserDetail>>,
    private val refreshUseCase: ReactiveUseCase<Id, Result<Boolean>>,
    private val id: String
) :
    ViewModel() {

    val userDetail: Flow<Result<UserDetailModel>> =
        getUserUseCase(Id(id)).map { result ->
            result.map {
                it.toUi()
            }
        }

    fun refresh(): Flow<Result<Boolean>> = refreshUseCase(Id(id))
}
