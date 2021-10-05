package com.sdelaherche.fairmoneytest.userlist.presentation

sealed class UserListResponseModel

object Loading : UserListResponseModel()
class Success(val list: List<UserModel>) : UserListResponseModel()
class Failure(val message: String? = null) : UserListResponseModel()