package com.sdelaherche.fairmoneytest.userdetail.domain.repository

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.UserDetail
import kotlinx.coroutines.flow.Flow

interface IUserDetailRepository {
    fun getUserById(id: Id): Flow<Result<UserDetail>>
    suspend fun refresh(id: Id): Result<Boolean>
}
