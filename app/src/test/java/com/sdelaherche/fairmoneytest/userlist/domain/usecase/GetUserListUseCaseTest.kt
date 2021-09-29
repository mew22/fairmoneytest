package com.sdelaherche.fairmoneytest.userlist.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.mockutil.generateExceptionFromClass
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import java.net.URI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GetUserListUseCaseTest {

    @MockK
    private lateinit var userRepository: IUserRepository

    private lateinit var getUserListUseCase: GetUserListUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        getUserListUseCase = GetUserListUseCase(userRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Nested
    inner class GetUserList {
        @Test
        fun `Try to fetch user list from repository without error`() = runBlocking {
            val fakeUser = User(
                id = Id("id"),
                title = Title("title"),
                firstName = Name("firstName"),
                lastName = Name("lastName"),
                picture = URI.create("http://test.com")
            )
            every {
                userRepository.getUserList()
            } returns flow {
                emit(Result.success(listOf(fakeUser)))
            }

            val result = getUserListUseCase().first()
            assertTrue(result.isSuccess && result.getOrNull()?.contains(fakeUser) == true)
        }

        @ParameterizedTest
        @ValueSource(
            classes = [
                ApiException::class, NoInternetException::class, UnexpectedException::class
            ]
        )
        fun `Try to spread exception while fetching user list from repository with an error`(
            exceptionClass: Class<Exception>
        ) =
            runBlocking {
                val exceptionInstance: Exception = generateExceptionFromClass(exceptionClass)

                every {
                    userRepository.getUserList()
                } returns flow {
                    emit(Result.failure<List<User>>(exceptionInstance))
                }

                val result = getUserListUseCase().first()
                assertTrue(
                    result.isFailure && result.getOrNull()
                        .isNullOrEmpty() && result.exceptionOrNull() == exceptionInstance
                )
            }
    }
}
