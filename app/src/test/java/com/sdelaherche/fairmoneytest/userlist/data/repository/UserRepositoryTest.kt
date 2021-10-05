package com.sdelaherche.fairmoneytest.userlist.data.repository

import com.sdelaherche.fairmoneytest.common.data.local.IUserLocalDataSource
import com.sdelaherche.fairmoneytest.common.data.model.UserData
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.userlist.data.remote.IUserRemoteService
import com.sdelaherche.fairmoneytest.userlist.data.remote.ResponseRemoteData
import com.sdelaherche.fairmoneytest.userlist.data.remote.UserRemoteData
import com.sdelaherche.fairmoneytest.userlist.domain.entity.Refreshing
import com.sdelaherche.fairmoneytest.userlist.domain.entity.RefreshingError
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserList
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URI

class UserRepositoryTest {

    @MockK
    private lateinit var userLocalSource: IUserLocalDataSource

    @MockK
    private lateinit var userRemoteService: IUserRemoteService

    private lateinit var userRepository: IUserRepository

    private val fakeUserDataList = mutableListOf<UserData>().apply {
        for (i in 0 until 50) {
            add(
                UserData(
                    id = "id$i",
                    title = "title",
                    firstName = "firstName",
                    lastName = "lastName",
                    picture = "http://test.com",
                    detail = null
                )
            )
        }
    }
    private val fakeUserList = mutableListOf<User>().apply {
        for (i in 0 until 50) {
            add(
                User(
                    id = Id("id$i"),
                    title = Title("title"),
                    firstName = Name("firstName"),
                    lastName = Name("lastName"),
                    picture = URI.create("http://test.com")
                )
            )
        }
    }

    private val fakeUserRemoteDataList = mutableListOf<UserRemoteData>().apply {
        for (i in 0 until 50) {
            add(
                UserRemoteData(
                    id = "id$i", title = "title", firstName = "firstName",
                    lastName = "lastName", picture = "http://test.com"
                )
            )
        }
    }

    private val fakeResponseRemoteData = ResponseRemoteData(
        data = fakeUserRemoteDataList, page = 1, total = 99, limit = 50
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        userRepository = UserRepository(
            remoteSource = userRemoteService,
            localSource = userLocalSource
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Nested
    inner class GetUserList {
        @Test
        fun `Try to fetch a list of user with cache`() = runBlocking {
            every {
                userLocalSource.getUsers()
            } returns flow {
                emit(fakeUserDataList)
            }

            val result = userRepository.getUserList().first()

            verify(exactly = 1) {
                userLocalSource.getUsers()
            }
            Assertions.assertTrue(result is UserList && result.list == fakeUserList)
        }

        @Test
        fun `Try to fetch a list of user without cache and store it in cache`() =
            runBlocking {

                val flow = MutableStateFlow<List<UserData>>(emptyList())

                every {
                    userLocalSource.getUsers()
                } returns flow

                coEvery {
                    userRemoteService.getUsers(any(), any())
                } returns fakeResponseRemoteData

                coEvery {
                    userLocalSource.insertUserList(fakeUserDataList)
                } coAnswers {
                    flow.value = fakeUserDataList
                    fakeUserDataList.mapIndexed { i, _ ->
                        i.toLong()
                    }
                }

                val result = userRepository.getUserList().take(2).toList()
                val shouldReturnRefreshing = result[0]
                val shouldReturnList = result[1]

                verify(exactly = 1) {
                    userLocalSource.getUsers()
                }
                coVerify(exactly = 1) {
                    userRemoteService.getUsers(any(), any())
                }
                coVerify(exactly = 1) {
                    userLocalSource.insertUserList(fakeUserDataList)
                }
                Assertions.assertTrue(
                    shouldReturnRefreshing is Refreshing &&
                            shouldReturnList is UserList && shouldReturnList.list == fakeUserList
                )
            }

        @Test
        fun `Try to spread a NoInternetException while fetching a list of user without cache and without internet`() =
            // ktlint-disable max-line-length
            runBlocking {
                every {
                    userLocalSource.getUsers()
                } returns flow {
                    emit(emptyList<UserData>())
                }

                coEvery {
                    userRemoteService.getUsers(any(), any())
                } throws NoInternetException()

                val result = userRepository.getUserList().first()

                verify(exactly = 1) {
                    userLocalSource.getUsers()
                }
                coVerify(exactly = 1) {
                    userRemoteService.getUsers(any(), any())
                }
                coVerify(exactly = 0) {
                    userLocalSource.insertUser(any())
                }

                Assertions.assertTrue(
                    result is RefreshingError && result.ex == NoInternetException()
                )
            }

        @Test
        fun `Try to spread a ApiException while fetching a list of user without cache and without internet`() =
            // ktlint-disable max-line-length
            runBlocking {
                every {
                    userLocalSource.getUsers()
                } returns flow {
                    emit(emptyList<UserData>())
                }

                coEvery {
                    userRemoteService.getUsers(any(), any())
                } throws ApiException("404")

                val result = userRepository.getUserList().first()

                verify(exactly = 1) {
                    userLocalSource.getUsers()
                }
                coVerify(exactly = 1) {
                    userRemoteService.getUsers(any(), any())
                }
                coVerify(exactly = 0) {
                    userLocalSource.insertUser(any())
                }

                Assertions.assertTrue(
                    result is RefreshingError && result.ex == ApiException("404")
                )
            }
    }

    @Nested
    inner class RefreshById {
        @Test
        fun `Try to refresh a list of user and store it in cache`() = runBlocking {

            coEvery {
                userRemoteService.getUsers(any(), any())
            } returns fakeResponseRemoteData

            coEvery {
                userLocalSource.insertUserList(fakeUserDataList)
            } returns fakeUserDataList.mapIndexed { i, _ ->
                i.toLong()
            }

            val result = userRepository.refresh()

            coVerify(exactly = 1) {
                userRemoteService.getUsers(any(), any())
            }
            coVerify(exactly = 1) {
                userLocalSource.insertUserList(fakeUserDataList)
            }

            Assertions.assertTrue(
                result.isSuccess && result.getOrNull() == true
            )
        }

        @Test
        fun `Try to spread NoInternetException by refreshing a list of user without internet`() =
            runBlocking {

                coEvery {
                    userRemoteService.getUsers(any(), any())
                } throws NoInternetException()

                val result = userRepository.refresh()

                coVerify(exactly = 1) {
                    userRemoteService.getUsers(any(), any())
                }
                coVerify(exactly = 0) {
                    userLocalSource.insertUserList(any())
                }

                Assertions.assertTrue(
                    result.isFailure && result.exceptionOrNull() == NoInternetException()
                )
            }

        @Test
        fun `Try to spread ApiException by refreshing a list of user with remote error`() =
            runBlocking {

                coEvery {
                    userRemoteService.getUsers(any(), any())
                } throws ApiException(statusCode = "404")

                val result = userRepository.refresh()

                coVerify(exactly = 1) {
                    userRemoteService.getUsers(any(), any())
                }
                coVerify(exactly = 0) {
                    userLocalSource.insertUserList(any())
                }

                Assertions.assertTrue(
                    result.isFailure && result.exceptionOrNull() == ApiException(statusCode = "404")
                )
            }
    }
}
