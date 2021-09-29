package com.sdelaherche.fairmoneytest.userdetail.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.mockutil.generateExceptionFromClass
import com.sdelaherche.fairmoneytest.userdetail.domain.failure.UnknownUserException
import com.sdelaherche.fairmoneytest.userdetail.domain.repository.IUserDetailRepository
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

class RefreshUserDetailUseCaseTest {

    @MockK
    private lateinit var userDetailRepository: IUserDetailRepository

    private lateinit var refreshUserDetailUseCase: RefreshUserDetailUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        refreshUserDetailUseCase = RefreshUserDetailUseCase(userDetailRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Nested
    inner class RefreshDetail {
        @Test
        fun `Try to refresh user by its Id from repository without error`() = runBlocking {
            val userId = Id("any")
            coEvery {
                userDetailRepository.refresh(userId)
            } returns Result.success(true)

            val result = refreshUserDetailUseCase(userId).first()
            Assertions.assertTrue(result.isSuccess && result.getOrNull() == true)
        }

        @ParameterizedTest
        @ValueSource(
            classes = [
                ApiException::class, NoInternetException::class,
                UnexpectedException::class, UnknownUserException::class
            ]
        )

        fun `Try to refresh user by its Id from repository with an error`(
            exceptionClass: Class<Exception>
        ) =
            runBlocking {
                val userId = Id("any")
                val exceptionInstance: Exception =
                    generateExceptionFromClass(exceptionClass, userId)

                coEvery {
                    userDetailRepository.refresh(userId)
                } returns Result.failure<Boolean>(exceptionInstance)

                val result = refreshUserDetailUseCase(userId).first()
                Assertions.assertTrue(
                    result.isFailure && result.getOrNull() == null &&
                            result.exceptionOrNull() == exceptionInstance
                )

                if (exceptionClass == UnknownUserException::class.javaObjectType) {
                    Assertions.assertTrue(
                        (exceptionInstance as UnknownUserException).userId == userId
                    )
                }

            }
    }
}
