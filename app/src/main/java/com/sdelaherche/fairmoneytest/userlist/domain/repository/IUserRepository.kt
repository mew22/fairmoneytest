package com.sdelaherche.fairmoneytest.userlist.domain.repository

import com.sdelaherche.fairmoneytest.common.util.Result
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserListResponse
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUserList(): Flow<UserListResponse>
    suspend fun refresh(): Result<Unit>
}
