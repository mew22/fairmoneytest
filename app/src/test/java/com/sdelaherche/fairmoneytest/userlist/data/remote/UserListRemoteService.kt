package com.sdelaherche.fairmoneytest.userlist.data.remote

import com.google.gson.Gson
import com.sdelaherche.fairmoneytest.common.data.remote.Config
import com.sdelaherche.fairmoneytest.common.data.remote.ErrorsCallAdapterFactory
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.mockutil.MockResponseFileReader
import com.sdelaherche.fairmoneytest.mockutil.enqueueResponse
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

class UserListRemoteService {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteService: IUserRemoteService

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        remoteService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(ErrorsCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IUserRemoteService::class.java)
    }

    @Nested
    inner class GetUserList {
        @Test
        fun `Try to fetch user detail correctly given 200 response`() = runBlocking {
            val page = 1
            val limit = 100
            mockWebServer.enqueueResponse("user_list_remote_mock_response.json", 200)
            val expected: ResponseRemoteData = Gson().fromJson(
                MockResponseFileReader("user_list_remote_mock_response.json").content,
                ResponseRemoteData::class.java
            )
            assertEquals(expected, remoteService.getUsers(page, limit))

            checkRequest(page, limit, mockWebServer.takeRequest())
        }

        @Test
        fun `Try to spread not found user exception given 404 response`(): Unit = runBlocking {
            val page = 1
            val limit = 100
            mockWebServer.enqueueResponse("user_list_remote_mock_response.json", 404)
            mockWebServer.requireClientAuth()
            assertThrows<ApiException> {
                remoteService.getUsers(page, limit)
            }

            checkRequest(page, limit, mockWebServer.takeRequest())
        }

        @Test
        fun `Try to spread no internet exception given no server`(): Unit = runBlocking {
            mockWebServer.shutdown()
            assertThrows<NoInternetException> {
                remoteService.getUsers(1, 100)
            }
            assertEquals(0, mockWebServer.requestCount)
        }
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun checkRequest(page: Int, limit: Int, request: RecordedRequest) {
        assertEquals("/data/v1/user", request.requestUrl?.encodedPath)
        assertEquals("GET", request.method)
        assertEquals(page.toString(), request.requestUrl?.queryParameter("page"))
        assertEquals(limit.toString(), request.requestUrl?.queryParameter("limit"))
        assertEquals(Config.API_KEY, request.headers[Config.HEADER_API_KEY])
    }
}