package com.sdelaherche.fairmoneytest.userdetail.data.repository

import com.sdelaherche.fairmoneytest.common.data.local.IUserLocalDataSource
import com.sdelaherche.fairmoneytest.common.data.model.LocationData
import com.sdelaherche.fairmoneytest.common.data.model.UserData
import com.sdelaherche.fairmoneytest.common.data.model.UserDetailData
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.userdetail.data.remote.IUserDetailRemoteService
import com.sdelaherche.fairmoneytest.userdetail.data.remote.LocationRemoteData
import com.sdelaherche.fairmoneytest.userdetail.data.remote.UserDetailRemoteData
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.*
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.TimeZone
import com.sdelaherche.fairmoneytest.userdetail.domain.failure.UnknownUserException
import com.sdelaherche.fairmoneytest.userdetail.domain.repository.IUserDetailRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import java.net.URI
import java.util.*

class UserDetailRepositoryTest {

    @MockK
    private lateinit var userDetailLocalSource: IUserLocalDataSource

    @MockK
    private lateinit var userDetailRemoteService: IUserDetailRemoteService

    private lateinit var userDetailRepository: IUserDetailRepository

    private val birthdate = Date()
    private val registerDate = Date()
    private val updatedDate = Date()

    val fakeInitialUserData = UserData(
        id = "id", title = "title", firstName = "firstName",
        lastName = "lastName", picture = "http://test.com",
        detail = null
    )
    private val fakeUserData = UserData(
        id = "id", title = "title", firstName = "firstName",
        lastName = "lastName", picture = "http://test.com",
        detail = UserDetailData(
            gender = Gender.MALE.toString().lowercase(),
            email = "email@email.com",
            phone = "0123456789",
            birthDate = birthdate,
            registerDate = registerDate,
            updatedDate = updatedDate,
            location = LocationData(
                street = "street",
                city = "city",
                state = "state",
                country = "country",
                timezone = "timezone"
            )
        )
    )
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

    private val fakeUserDetailRemoteData = UserDetailRemoteData(
        id = "id", title = "title", firstName = "firstName",
        lastName = "lastName", picture = "http://test.com",
        gender = Gender.MALE.toString().lowercase(),
        email = "email@email.com",
        phone = "0123456789",
        birthdate = birthdate,
        registerDate = registerDate,
        updatedDate = updatedDate,
        location = LocationRemoteData(
            street = "street",
            city = "city",
            state = "state",
            country = "country",
            timezone = "timezone"
        )
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        userDetailRepository = UserDetailRepository(
            remoteSource = userDetailRemoteService,
            localSource = userDetailLocalSource
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Nested
    inner class GetUserById {
        @Test
        fun `Try to fetch an user detail by its Id with cache`() = runBlocking {

            every {
                userDetailLocalSource.getUserById(fakeUserData.id)
            } returns flow {
                emit(fakeUserData)
            }

            val result = userDetailRepository.getUserById(Id(fakeUserData.id)).first()

            verify(exactly = 1) {
                userDetailLocalSource.getUserById(fakeUserData.id)
            }
            Assertions.assertEquals(fakeUserDetail, result.getOrNull())
        }

        @Test
        fun `Try to fetch an user detail with a wrong Id with cache`() = runBlocking {
            val userId = Id("any")
            every {
                userDetailLocalSource.getUserById(userId.value)
            } returns flow {
                emit(null)
            }
            val result = userDetailRepository.getUserById(userId).first()

            verify(exactly = 1) {
                userDetailLocalSource.getUserById(userId.value)
            }
            Assertions.assertTrue(
                result.isFailure && result.exceptionOrNull() == UnknownUserException(userId)
            )
        }

        @Test
        fun `Try to fetch an user detail by its Id without cache and store it in cache`() = runBlocking {

            val flow = MutableStateFlow(fakeInitialUserData)

            every {
                userDetailLocalSource.getUserById(fakeInitialUserData.id)
            } returns flow

            coEvery {
                userDetailRemoteService.getUserById(fakeInitialUserData.id)
            } returns fakeUserDetailRemoteData

            coEvery {
                userDetailLocalSource.updateUser(fakeUserData)
            } coAnswers {
                flow.value = fakeUserData
                1 // return of updateUser
            }

            val result = userDetailRepository.getUserById(Id(fakeInitialUserData.id)).first()

            verify(exactly = 1) {
                userDetailLocalSource.getUserById(fakeInitialUserData.id)
            }
            coVerify(exactly = 1) {
                userDetailRemoteService.getUserById(fakeInitialUserData.id)
            }
            coVerify(exactly = 1) {
                userDetailLocalSource.updateUser(fakeUserData)
            }
            Assertions.assertTrue(
                result.isSuccess && result.getOrNull() == fakeUserDetail
            )
        }

        @Test
        fun `Try to fetch an user detail with a wrong Id without cache and check cache is not altered`() = runBlocking {
            val wrongUserId = "any"
            every {
                userDetailLocalSource.getUserById(wrongUserId)
            } returns flow{
                emit(null)
            }

            val result = userDetailRepository.getUserById(Id(wrongUserId)).first()

            verify(exactly = 1) {
                userDetailLocalSource.getUserById(wrongUserId)
            }
            coVerify (exactly = 0) {
                userDetailLocalSource.updateUser(any())
            }
            Assertions.assertTrue(
                result.isFailure && result.exceptionOrNull() == UnknownUserException(Id(wrongUserId))
            )
        }

        @Test
        fun `Try to fetch an user detail by its Id without cache and without internet`() = runBlocking {
            every {
                userDetailLocalSource.getUserById(fakeInitialUserData.id)
            } returns flow{
                emit(fakeInitialUserData)
            }

            coEvery {
                userDetailRemoteService.getUserById(fakeInitialUserData.id)
            } throws NoInternetException()

            val result = userDetailRepository.getUserById(Id(fakeInitialUserData.id)).first()

            verify(exactly = 1) {
                userDetailLocalSource.getUserById(fakeInitialUserData.id)
            }
            coVerify(exactly = 1) {
                userDetailRemoteService.getUserById(fakeInitialUserData.id)
            }
            coVerify (exactly = 0) {
                userDetailLocalSource.updateUser(any())
            }

            Assertions.assertTrue(
                result.isFailure && result.exceptionOrNull() == NoInternetException()
            )
        }
    }

    @Nested
    inner class RefreshById {
        @Test
        fun `Try to refresh an user detail by its Id and store it in cache`() = runBlocking {

            coEvery {
                userDetailRemoteService.getUserById(fakeInitialUserData.id)
            } returns fakeUserDetailRemoteData

            coEvery {
                userDetailLocalSource.updateUser(fakeUserData)
            } returns 1

            val result = userDetailRepository.refresh(Id(fakeInitialUserData.id))

            coVerify(exactly = 1) {
                userDetailRemoteService.getUserById(fakeInitialUserData.id)
            }
            coVerify(exactly = 1) {
                userDetailLocalSource.updateUser(fakeUserData)
            }

            Assertions.assertTrue(
                result.isSuccess && result.getOrNull() == true
            )

        }

        @Test
        fun `Try to spread UnknownUserException by refreshing an user detail with a wrong Id and check cache is not altered`() = runBlocking {
            val wrongUserId = "any"
            coEvery {
                userDetailRemoteService.getUserById(wrongUserId)
            } throws UnknownUserException(Id(wrongUserId))

            val result = userDetailRepository.refresh(Id(wrongUserId))

            coVerify(exactly = 1) {
                userDetailRemoteService.getUserById(wrongUserId)
            }
            coVerify (exactly = 0) {
                userDetailLocalSource.updateUser(any())
            }
            Assertions.assertTrue(
                result.isFailure && result.exceptionOrNull() == UnknownUserException(Id(wrongUserId))
            )
        }

        @Test
        fun `Try to spread NoInternetException by refreshing an user detail by its Id without internet`() = runBlocking {

            coEvery {
                userDetailRemoteService.getUserById(fakeInitialUserData.id)
            } throws NoInternetException()

            val result = userDetailRepository.refresh(Id(fakeInitialUserData.id))

            coVerify(exactly = 1) {
                userDetailRemoteService.getUserById(fakeInitialUserData.id)
            }
            coVerify (exactly = 0) {
                userDetailLocalSource.updateUser(any())
            }

            Assertions.assertTrue(
                result.isFailure && result.exceptionOrNull() == NoInternetException()
            )
        }
    }
}