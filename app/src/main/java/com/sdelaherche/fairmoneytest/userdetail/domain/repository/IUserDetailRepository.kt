package com.sdelaherche.fairmoneytest.userdetail.domain.repository

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.util.Result
import com.sdelaherche.fairmoneytest.userdetail.domain.UserDetailResponse
import kotlinx.coroutines.flow.Flow

interface IUserDetailRepository {
    fun getUserById(id: Id): Flow<UserDetailResponse>
    suspend fun refresh(id: Id): Result<Unit>
}
