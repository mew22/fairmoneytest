package com.sdelaherche.fairmoneytest.common.presentation

sealed class RefreshResponseModel

class RefreshSuccess(val message: String) : RefreshResponseModel()
class RefreshFailure(val message: String) : RefreshResponseModel()
