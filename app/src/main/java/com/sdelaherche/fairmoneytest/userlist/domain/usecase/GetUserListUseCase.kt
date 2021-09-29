package com.sdelaherche.fairmoneytest.userlist.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class GetUserListUseCase(private val userRepository: IUserRepository) :
    ReactiveUseCase<Unit, Result<List<User>>> {

    override fun invoke(params: Unit?): Flow<Result<List<User>>> =
        userRepository.getUserList()
}