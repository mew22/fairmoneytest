package com.sdelaherche.fairmoneytest.userlist.domain.entity

import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException

sealed class UserListResponse

object Refreshing : UserListResponse()
class UserList(val list: List<User>) : UserListResponse()
class RefreshingError(val ex: DomainException) : UserListResponse()