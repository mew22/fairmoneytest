package com.sdelaherche.fairmoneytest.userlist.data.remote

import com.sdelaherche.fairmoneytest.common.data.remote.Config
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IUserRemoteService {
    companion object {
        private const val ENDPOINT = "data/v1/user"
    }

    @Headers("${Config.HEADER_API_KEY}: ${Config.API_KEY}")
    @GET(ENDPOINT)
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ResponseRemoteData
}
