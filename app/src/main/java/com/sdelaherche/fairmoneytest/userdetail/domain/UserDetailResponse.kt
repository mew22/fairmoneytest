package com.sdelaherche.fairmoneytest.userdetail.domain

import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.UserDetail

sealed class UserDetailResponse

object Refreshing : UserDetailResponse()
class Detail(val detail: UserDetail) : UserDetailResponse()
class RefreshingError(val ex: DomainException) : UserDetailResponse()
