package com.sdelaherche.fairmoneytest.userlist.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.usecase.SuspendUseCase
import com.sdelaherche.fairmoneytest.common.util.Result
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository

class RefreshUserListUseCase(private val userRepository: IUserRepository) :
    SuspendUseCase<Unit, Result<Unit>> {

    override suspend fun invoke(params: Unit?): Result<Unit> = userRepository.refresh()
}
