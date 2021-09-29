package com.sdelaherche.fairmoneytest.userdetail.data.remote

import com.google.gson.Gson
import com.sdelaherche.fairmoneytest.common.data.remote.Config
import com.sdelaherche.fairmoneytest.common.data.remote.ErrorsCallAdapterFactory
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.mockutil.MockResponseFileReader
import com.sdelaherche.fairmoneytest.mockutil.enqueueResponse
import com.sdelaherche.fairmoneytest.userdetail.domain.failure.UnknownUserException
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockWebServer
import mockwebserver3.RecordedRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserDetailRemoteService {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteService: IUserDetailRemoteService

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        remoteService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(ErrorsCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IUserDetailRemoteService::class.java)
    }

    @Nested
    inner class GetUserDetail {
        @Test
        fun `Try to fetch user detail correctly given 200 response`() = runBlocking {
            val userId = "any"
            mockWebServer.enqueueResponse(
                "user_detail_remote_mock_response.json",
                200
            )
            val expected: UserDetailRemoteData = Gson().fromJson(
                MockResponseFileReader("user_detail_remote_mock_response.json").content,
                UserDetailRemoteData::class.java
            )
            assertEquals(expected, remoteService.getUserById(userId))

            checkRequest(userId, mockWebServer.takeRequest())
        }

        @Test
        fun `Try to spread not found user exception given 404 response`(): Unit = runBlocking {
            val userId = "any"
            mockWebServer.enqueueResponse(
                "user_detail_remote_mock_response.json",
                404
            )
            assertThrows<UnknownUserException> {
                remoteService.getUserById(userId)
            }
            checkRequest(userId, mockWebServer.takeRequest())
        }

        @Test
        fun `Try to spread no internet exception given no server`(): Unit = runBlocking {
            val userId = "any"
            mockWebServer.shutdown()
            assertThrows<NoInternetException> {
                remoteService.getUserById(userId)
            }
            assertEquals(0, mockWebServer.requestCount)
        }
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun checkRequest(userId: String, request: RecordedRequest) {
        assertEquals("/data/v1/user/$userId", request.path)
        assertEquals("GET", request.method)
        assertEquals(Config.API_KEY, request.headers[Config.HEADER_API_KEY])
    }
}
