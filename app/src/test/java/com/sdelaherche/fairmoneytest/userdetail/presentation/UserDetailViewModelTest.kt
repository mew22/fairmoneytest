package com.sdelaherche.fairmoneytest.userdetail.presentation

import android.app.Application
import android.net.Uri
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.failure.UserNotFoundException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.common.presentation.RefreshFailure
import com.sdelaherche.fairmoneytest.common.presentation.RefreshSuccess
import com.sdelaherche.fairmoneytest.common.util.Result
import com.sdelaherche.fairmoneytest.mockutil.generateExceptionFromClass
import com.sdelaherche.fairmoneytest.userdetail.domain.Detail
import com.sdelaherche.fairmoneytest.userdetail.domain.Refreshing
import com.sdelaherche.fairmoneytest.userdetail.domain.RefreshingError
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
import com.sdelaherche.fairmoneytest.userdetail.domain.usecase.GetUserDetailUseCase
import com.sdelaherche.fairmoneytest.userdetail.domain.usecase.RefreshUserDetailUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
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
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class UserDetailViewModelTest {

    @MockK
    private lateinit var getUserDetailUseCase: GetUserDetailUseCase

    @MockK
    private lateinit var refreshDetailUseCase: RefreshUserDetailUseCase

    @RelaxedMockK
    private lateinit var application: Application

    @MockK
    private lateinit var userUri: Uri

    private lateinit var userDetailViewModel: UserDetailViewModel
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
                emit(Detail(detail = fakeUserDetail))
            }

            initViewModel()

            val result =
                userDetailViewModel.userDetail.first()

            Assertions.assertTrue(result is Success && fakeUserDetailModel == result.detail)
        }

        @Test
        fun `Try to provide loading state while fetching user detail model from usecase without cache`() = runBlocking {
            every {
                getUserDetailUseCase(fakeUserDetail.user.id)
            } returns flow {
                emit(Refreshing)
            }

            initViewModel()

            val result =
                userDetailViewModel.userDetail.first()

            Assertions.assertTrue(result is Loading)
        }

        @ParameterizedTest
        @ValueSource(
            classes = [
                ApiException::class,
                NoInternetException::class,
                UnexpectedException::class,
                UserNotFoundException::class
            ]
        )
        fun `Try to spread exception while fetching user detail by its Id from usecase with an error`( // ktlint-disable max-line-length
            exceptionClass: Class<DomainException>
        ) =
            runBlocking {
                val exceptionInstance: DomainException =
                    generateExceptionFromClass(exceptionClass, fakeUserDetail.user.id)

                every {
                    getUserDetailUseCase(fakeUserDetail.user.id)
                } returns flow {
                    emit(RefreshingError(exceptionInstance))
                }

                initViewModel()

                val result =
                    userDetailViewModel.userDetail.first()

                Assertions.assertTrue(
                    result is Failure
                )
            }
    }

    @Nested
    inner class RefreshDetail {
        @Test
        fun `Try to refresh user detail model by its Id`() = runBlocking {

            coEvery {
                refreshDetailUseCase(fakeUserDetail.user.id)
            } returns Result.success(Unit)

            every {
                getUserDetailUseCase(fakeUserDetail.user.id)
            } returns flow {
                emit(Detail(detail = fakeUserDetail))
            }

            initViewModel()

            val result = userDetailViewModel.refresh()
            Assertions.assertTrue(result is RefreshSuccess)
        }

        @ParameterizedTest
        @ValueSource(
            classes = [
                ApiException::class,
                NoInternetException::class,
                UnexpectedException::class,
                UserNotFoundException::class
            ]
        )
        fun `Try to refresh user detail model by its Id from usecase with an error`(
            exceptionClass: Class<DomainException>
        ) = runBlocking {
            val exceptionInstance: DomainException =
                generateExceptionFromClass(exceptionClass, fakeUserDetail.user.id)

            coEvery {
                refreshDetailUseCase(fakeUserDetail.user.id)
            } returns Result.failure(exceptionInstance)

            every {
                getUserDetailUseCase(fakeUserDetail.user.id)
            } returns flow {
                emit(Detail(detail = fakeUserDetail))
            }
            initViewModel()

            val result = userDetailViewModel.refresh()
            Assertions.assertTrue(
                result is RefreshFailure
            )
        }
    }

    private fun initViewModel() {
        userDetailViewModel = UserDetailViewModel(
            getUserUseCase = getUserDetailUseCase,
            refreshUseCase = refreshDetailUseCase,
            id = fakeUserDetail.user.id.value,
            application = application
        )
    }
}
