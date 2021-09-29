package com.sdelaherche.fairmoneytest.userdetail.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import com.sdelaherche.fairmoneytest.userdetail.domain.repository.IUserDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RefreshUserDetailUseCase(private val userDetailRepository: IUserDetailRepository) :
    ReactiveUseCase<Id, Result<Boolean>> {
    override fun invoke(params: Id?): Flow<Result<Boolean>> {
        return params?.let { id ->
            flow {
                emit(userDetailRepository.refresh(id))
            }
        } ?: throw IllegalArgumentException("Id argument is mandatory")
    }
}
