package com.sdelaherche.fairmoneytest.userlist.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserListResponse
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class GetUserListUseCase(private val userRepository: IUserRepository) :
    ReactiveUseCase<Unit, UserListResponse> {

    override fun invoke(params: Unit?): Flow<UserListResponse> =
        userRepository.getUserList()
}