/* ktlint-disable max-line-length */

package com.sdelaherche.fairmoneytest.userdetail.domain.usecase

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.mockutil.generateExceptionFromClass
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.City
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Country
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.EmailAddress
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Gender
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Location
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.PhoneNumber
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.State
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Street
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.TimeZone
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.UserDetail
import com.sdelaherche.fairmoneytest.userdetail.domain.failure.UnknownUserException
import com.sdelaherche.fairmoneytest.userdetail.domain.repository.IUserDetailRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import java.net.URI
import java.util.Date
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GetUserDetailUseCaseTest {

    @MockK
    private lateinit var userDetailRepository: IUserDetailRepository

    private lateinit var getUserDetailUseCase: GetUserDetailUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        getUserDetailUseCase = GetUserDetailUseCase(userDetailRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Nested
    inner class GetUserDetail {
        @Test
        fun `Try to fetch an user detail by its Id from repository without error`() = runBlocking {
            val fakeUserDetail = UserDetail(
                user = User(
                    id = Id("id"),
                    title = Title("title"),
                    firstName = Name("firstName"),
                    lastName = Name("lastName"),
                    picture = URI.create("http://test.com")
                ),
                gender = Gender.MALE,
                email = EmailAddress("email@email.com"),
                phone = PhoneNumber("0123456789"),
                birthDate = Date(),
                registerDate = Date(),
                updatedDate = Date(),
                location = Location(
                    street = Street("street"),
                    city = City("city"),
                    state = State("state"),
                    country = Country("country"),
                    timezone = TimeZone("timezone")
                )
            )
            every {
                userDetailRepository.getUserById(fakeUserDetail.user.id)
            } returns flow {
                emit(Result.success(fakeUserDetail))
            }

            val result = getUserDetailUseCase(fakeUserDetail.user.id).first()
            Assertions.assertTrue(result.isSuccess && result.getOrNull() == fakeUserDetail)
        }

        @ParameterizedTest
        @ValueSource(
            classes = [
                ApiException::class,
                NoInternetException::class,
                UnexpectedException::class,
                UnknownUserException::class
            ]
        )
        fun `Try to spread exception while fetching an user detail by its Id from repository with an error`(
            exceptionClass: Class<Exception>
        ) = runBlocking {
            val userId = Id("any")
            val exceptionInstance: Exception = generateExceptionFromClass(exceptionClass, userId)

            every {
                userDetailRepository.getUserById(userId)
            } returns flow {
                emit(Result.failure<UserDetail>(exceptionInstance))
            }

            val result = getUserDetailUseCase(userId).first()
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
