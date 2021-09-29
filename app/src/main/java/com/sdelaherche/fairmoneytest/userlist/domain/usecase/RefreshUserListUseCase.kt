package com.sdelaherche.fairmoneytest.userlist.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RefreshUserListUseCase(private val userRepository: IUserRepository) :
    ReactiveUseCase<Unit, Result<Boolean>> {

    override fun invoke(params: Unit?): Flow<Result<Boolean>> {
        return flow {
            emit(userRepository.refresh())
        }
    }
}
