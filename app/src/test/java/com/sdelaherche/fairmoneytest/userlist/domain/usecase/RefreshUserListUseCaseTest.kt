package com.sdelaherche.fairmoneytest.userlist.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.mockutil.generateExceptionFromClass
import com.sdelaherche.fairmoneytest.userlist.domain.repository.IUserRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class RefreshUserListUseCaseTest {

    @MockK
    private lateinit var userRepository: IUserRepository

    private lateinit var refreshUserListUseCase: RefreshUserListUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        refreshUserListUseCase = RefreshUserListUseCase(userRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Nested
    inner class RefreshList {
        @Test
        fun `Try to refresh user list from repository without error`() = runBlocking {
            coEvery {
                userRepository.refresh()
            } returns Result.success(true)

            val result = refreshUserListUseCase().first()
            Assertions.assertTrue(result.isSuccess && result.getOrNull() == true)
        }

        @ParameterizedTest
        @ValueSource(
            classes = [
                ApiException::class, NoInternetException::class, UnexpectedException::class
            ]
        )
        fun `Try to refresh user list from repository with an error`(exceptionClass: Class<Exception>) =
            runBlocking {
                val exceptionInstance: Exception = generateExceptionFromClass(exceptionClass)

                coEvery {
                    userRepository.refresh()
                } returns Result.failure<Boolean>(exceptionInstance)

                val result = refreshUserListUseCase().first()
                Assertions.assertTrue(
                    result.isFailure && result.getOrNull() == null && result.exceptionOrNull() == exceptionInstance
                )
            }
    }
}
