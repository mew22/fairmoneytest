package com.sdelaherche.fairmoneytest.userlist.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.mockutil.generateExceptionFromClass
import com.sdelaherche.fairmoneytest.userlist.domain.entity.Refreshing
import com.sdelaherche.fairmoneytest.userlist.domain.entity.RefreshingError
import com.sdelaherche.fairmoneytest.userlist.domain.entity.UserList
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
import java.net.URI

class GetUserListUseCaseTest {

    @MockK
    private lateinit var userRepository: IUserRepository

    private lateinit var getUserListUseCase: GetUserListUseCase

    private val fakeUserList = mutableListOf<User>().apply {
        for (i in 0 until 10) {
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
            every {
                userRepository.getUserList()
            } returns flow {
                emit(UserList(list = fakeUserList))
            }

            val result = getUserListUseCase().first()
            assertTrue(result is UserList && result.list == fakeUserList)
        }

        @Test
        fun `Try to provide refreshing state while fetching user list from repository without cache`() =
            runBlocking {
                every {
                    userRepository.getUserList()
                } returns flow {
                    emit(Refreshing)
                }

                val result = getUserListUseCase().first()
                assertTrue(result is Refreshing)
            }

        @ParameterizedTest
        @ValueSource(
            classes = [
                ApiException::class, NoInternetException::class, UnexpectedException::class
            ]
        )
        fun `Try to spread exception while fetching user list from repository with an error`(
            exceptionClass: Class<DomainException>
        ) =
            runBlocking {
                val exceptionInstance: DomainException = generateExceptionFromClass(exceptionClass)

                every {
                    userRepository.getUserList()
                } returns flow {
                    emit(RefreshingError(exceptionInstance))
                }

                val result = getUserListUseCase().first()
                assertTrue(
                    result is RefreshingError && result.ex == exceptionInstance
                )
            }
    }
}
