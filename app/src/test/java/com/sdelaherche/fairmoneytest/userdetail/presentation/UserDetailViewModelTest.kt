package com.sdelaherche.fairmoneytest.userdetail.presentation

import android.net.Uri
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
import com.sdelaherche.fairmoneytest.userdetail.domain.usecase.GetUserDetailUseCase
import com.sdelaherche.fairmoneytest.userdetail.domain.usecase.RefreshUserDetailUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

class UserDetailViewModelTest {

    @MockK
    private lateinit var getUserDetailUseCase: GetUserDetailUseCase

    @MockK
    private lateinit var refreshDetailUseCase: RefreshUserDetailUseCase

    private lateinit var userDetailViewModel: UserDetailViewModel

    @MockK
    private lateinit var userUri: Uri

    private val birthdate = Date()
    private val registerDate = Date()
    private val updatedDate = Date()

    private val fakeUserDetail = UserDetail(
        user = User(
            id = Id("id"), title = Title("title"), firstName = Name("firstName"),
            lastName = Name("lastName"), picture = URI.create("http://test.com")
        ),
        gender = Gender.MALE,
        email = EmailAddress("email@email.com"),
        phone = PhoneNumber("0123456789"),
        birthDate = birthdate,
        registerDate = registerDate,
        updatedDate = updatedDate,
        location = Location(
            street = Street("street"),
            city = City("city"),
            state = State("state"),
            country = Country("country"),
            timezone = TimeZone("timezone")
        )
    )

    private val fakeUserDetailModel by lazy {
        UserDetailModel(
            title = "title", firstName = "firstName",
            lastName = "lastName", picture = Uri.parse("http://test.com"),
            gender = Gender.MALE,
            email = "email@email.com",
            phone = "0123456789",
            birthDate = SimpleDateFormat(
                Config.READABLE_DATE_PATTERN,
                Locale.getDefault()
            ).format(birthdate),
            street = "street",
            city = "city",
            state = "state",
            country = "country",
            timezone = "timezone"

        )
    }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(Uri::class)
        every {
            Uri.parse("http://test.com")
        } returns userUri

        userDetailViewModel = UserDetailViewModel(
            getUserUseCase = getUserDetailUseCase,
            refreshUseCase = refreshDetailUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Uri::class)
        clearAllMocks()
    }

    @Nested
    inner class GetUserDetailById {
        @Test
        fun `Try to provide user detail ui model by its Id`() = runBlocking {
            every {
                getUserDetailUseCase(fakeUserDetail.user.id)
            } returns flow {
                emit(Result.success(fakeUserDetail))
            }

            val result =
                userDetailViewModel.getUserDetailFromId(fakeUserDetail.user.id.value).first()

            Assertions.assertTrue(result.isSuccess && fakeUserDetailModel == result.getOrNull())
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
        fun `Try to spread exception while fetching user detail by its Id from usecase with an error`( // ktlint-disable max-line-length
            exceptionClass: Class<Exception>
        ) =
            runBlocking {
                val exceptionInstance: Exception =
                    generateExceptionFromClass(exceptionClass, fakeUserDetail.user.id)

                every {
                    getUserDetailUseCase(fakeUserDetail.user.id)
                } returns flow {
                    emit(Result.failure<UserDetail>(exceptionInstance))
                }

                val result =
                    userDetailViewModel.getUserDetailFromId(fakeUserDetail.user.id.value).first()

                Assertions.assertTrue(
                    result.isFailure && result.getOrNull() == null &&
                        result.exceptionOrNull() == exceptionInstance
                )
            }
    }

    @Nested
    inner class RefreshDetail {
        @Test
        fun `Try to refresh user detail model by its Id`() = runBlocking {

            every {
                refreshDetailUseCase(fakeUserDetail.user.id)
            } returns flow {
                emit(Result.success(true))
            }

            val result = userDetailViewModel.refresh(fakeUserDetail.user.id.value).first()
            Assertions.assertTrue(result.isSuccess && result.getOrNull() == true)
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
        fun `Try to refresh user detail model by its Id from usecase with an error`(
            exceptionClass: Class<Exception>
        ) = runBlocking {
            val exceptionInstance: Exception =
                generateExceptionFromClass(exceptionClass, fakeUserDetail.user.id)

            every {
                refreshDetailUseCase(fakeUserDetail.user.id)
            } returns flow { emit(Result.failure<Boolean>(exceptionInstance)) }

            val result = userDetailViewModel.refresh(fakeUserDetail.user.id.value).first()
            Assertions.assertTrue(
                result.isFailure && result.getOrNull() == null &&
                    result.exceptionOrNull() == exceptionInstance
            )
        }
    }
}
