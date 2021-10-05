package com.sdelaherche.fairmoneytest.userdetail.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.usecase.ReactiveUseCase
import com.sdelaherche.fairmoneytest.userdetail.domain.UserDetailResponse
import com.sdelaherche.fairmoneytest.userdetail.domain.repository.IUserDetailRepository
import kotlinx.coroutines.flow.Flow

class GetUserDetailUseCase(private val userDetailRepository: IUserDetailRepository) :
    ReactiveUseCase<Id, UserDetailResponse> {
    override fun invoke(params: Id?): Flow<UserDetailResponse> {
        return params?.let { id ->
            userDetailRepository.getUserById(id)
        } ?: throw IllegalArgumentException("Id argument is mandatory")
    }
}