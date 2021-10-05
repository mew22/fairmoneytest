package com.sdelaherche.fairmoneytest.userdetail.data.repository

import com.sdelaherche.fairmoneytest.common.data.local.IUserLocalDataSource
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.failure.UserNotFoundException
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.userdetail.data.mapper.toEntity
import com.sdelaherche.fairmoneytest.userdetail.data.mapper.toLocal
import com.sdelaherche.fairmoneytest.userdetail.data.remote.IUserDetailRemoteService
import com.sdelaherche.fairmoneytest.userdetail.data.remote.UserDetailRemoteData
import com.sdelaherche.fairmoneytest.userdetail.domain.Detail
import com.sdelaherche.fairmoneytest.userdetail.domain.Refreshing
import com.sdelaherche.fairmoneytest.userdetail.domain.RefreshingError
import com.sdelaherche.fairmoneytest.userdetail.domain.UserDetailResponse
import com.sdelaherche.fairmoneytest.userdetail.domain.repository.IUserDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDetailRepository(
    private val remoteSource: IUserDetailRemoteService,
    private val localSource: IUserLocalDataSource
) : IUserDetailRepository {

    override fun getUserById(id: Id): Flow<UserDetailResponse> =
        localSource.getUserById(id.value).map {
            if (it != null) {
                it.toEntity()?.let { entity ->
                    Detail(detail = entity)
                } ?: refresh(id).fold(
                    onSuccess = {Refreshing},
                    onFailure = {ex -> RefreshingError(ex as DomainException)}
                )
            } else {
                RefreshingError(ex = UserNotFoundException(id))
            }
        }

    override suspend fun refresh(id: Id): Result<Boolean> = try {
        Result.success(insertFromRemote(remoteSource.getUserById(id.value)) != 0)
    } catch (ex: DomainException) {
        Result.failure(exception = ex)
    }

    private suspend fun insertFromRemote(data: UserDetailRemoteData): Int =
        localSource.updateUser(data.toLocal())
}
