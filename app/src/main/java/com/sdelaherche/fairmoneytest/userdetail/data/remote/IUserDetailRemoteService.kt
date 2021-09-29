package com.sdelaherche.fairmoneytest.userdetail.data.remote

import com.sdelaherche.fairmoneytest.common.data.mapper.ExceptionsMapper
import com.sdelaherche.fairmoneytest.common.data.remote.Config
import com.sdelaherche.fairmoneytest.userdetail.data.mapper.UnknownUserExceptionMapper
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface IUserDetailRemoteService {
    companion object {
        private const val ENDPOINT = "data/v1/user/{user_id}"
    }

    @Headers("${Config.HEADER_API_KEY}: ${Config.API_KEY}")
    @GET(ENDPOINT)
    @ExceptionsMapper(value = UnknownUserExceptionMapper::class)
    suspend fun getUserById(@Path("user_id") userId: String): UserDetailRemoteData
}
