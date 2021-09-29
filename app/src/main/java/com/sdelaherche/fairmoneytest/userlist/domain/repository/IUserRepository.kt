package com.sdelaherche.fairmoneytest.userlist.domain.repository

import com.sdelaherche.fairmoneytest.common.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUserList(): Flow<Result<List<User>>>
    suspend fun refresh(): Result<Boolean>
}
