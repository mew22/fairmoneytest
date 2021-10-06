package com.sdelaherche.fairmoneytest.userdetail.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.usecase.SuspendUseCase
import com.sdelaherche.fairmoneytest.userdetail.domain.repository.IUserDetailRepository

class RefreshUserDetailUseCase(private val userDetailRepository: IUserDetailRepository) :
    SuspendUseCase<Id, Result<Unit>> {
    override suspend fun invoke(params: Id?): Result<Unit> {
        return params?.let { id ->
            userDetailRepository.refresh(id)
        } ?: throw IllegalArgumentException("Id argument is mandatory")
    }
}
