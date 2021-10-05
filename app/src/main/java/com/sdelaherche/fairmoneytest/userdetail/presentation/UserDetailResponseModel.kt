package com.sdelaherche.fairmoneytest.userdetail.presentation

sealed class UserDetailResponseModel

object Loading : UserDetailResponseModel()
class Success(val detail: UserDetailModel) : UserDetailResponseModel()
class Failure(val message: String? = null) : UserDetailResponseModel()