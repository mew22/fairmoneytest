package com.sdelaherche.fairmoneytest.userlist.data.repository

import com.sdelaherche.fairmoneytest.common.data.local.IUserLocalDataSource
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.failure.LocalInsertionException
import com.sdelaherche.fairmoneytest.common.data.mapper.toEntity
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.userlist.data.mapper.toLocal
import com.sdelaherche.fairmoneytest.userlist.data.remote.IUserRemoteService
import com.sdelaherche.fairmoneytest.userlist.data.remote.UserRemoteData
import com.sdelaherche.fairmoneytest.userlist.domain.entity.Refreshing
import com.sdelaherche.fairmoneytest.userlist.domain.entity.RefreshingError
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserList
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserListResponse
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val remoteSource: IUserRemoteService,
    private val localSource: IUserLocalDataSource
) : IUserRepository {

    private companion object {
        private const val INITIAL_PAGE = 1
        private const val NUMBER_OF_USERS_PER_PAGE = 50
    }

    override fun getUserList(): Flow<UserListResponse> =
        localSource.getUsers().map {
            if (it.isNullOrEmpty()) {
                refresh().fold(
                    onSuccess = { Refreshing},
                    onFailure = {ex -> RefreshingError(ex as DomainException)}
                )
            } else {
                UserList(list = it.toEntity())
            }
        }

    override suspend fun refresh(): Result<Boolean> = try {
        Result.success(
            insertFromRemote(
                data = remoteSource.getUsers(
                    INITIAL_PAGE,
                    NUMBER_OF_USERS_PER_PAGE
                ).data
            ).isNotEmpty()
        )
    } catch (ex: DomainException) {
        Result.failure(exception = ex)
    }

    private suspend fun insertFromRemote(data: List<UserRemoteData>): List<Long> =
        localSource.insertUserList(data.toLocal())
}
