package com.sdelaherche.fairmoneytest.userlist.presentation

import android.net.Uri
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.mockutil.generateExceptionFromClass
import com.sdelaherche.fairmoneytest.userlist.domain.usecase.GetUserListUseCase
import com.sdelaherche.fairmoneytest.userlist.domain.usecase.RefreshUserListUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.net.URI
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

class UserListViewModelTest {

    @MockK
    private lateinit var getUserListUseCase: GetUserListUseCase

    @MockK
    private lateinit var refreshListUseCase: RefreshUserListUseCase

    private lateinit var userListViewModel: UserListViewModel

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

    private val fakeUserUiModelList by lazy {
        mutableListOf<UserModel>().apply {
            for (i in 0 until 10) {
                add(
                    UserModel(
                        id = "id$i", title = "title", firstName = "firstName",
                        lastName = "lastName", picture = Uri.parse("http://test.com")
                    )
                )
            }
        }
    }

    @MockK
    private lateinit var userUri: Uri

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
    inner class UserList {
        @Test
        fun `Try to provide user ui model list`() = runBlocking {
            every {
                getUserListUseCase()
            } returns flow {
                emit(Result.success(fakeUserList))
            }

            initViewModel()

            val result = userListViewModel.userList.first()

            Assertions.assertTrue(result.isSuccess && fakeUserUiModelList == result.getOrNull())
        }

        @ParameterizedTest
        @ValueSource(
            classes = [
                ApiException::class, NoInternetException::class, UnexpectedException::class
            ]
        )
        fun `Try to spread exception while fetching user list model from usecase with an error`(
            exceptionClass: Class<Exception>
        ) =
            runBlocking {
                val exceptionInstance: Exception = generateExceptionFromClass(exceptionClass)

                every {
                    getUserListUseCase()
                } returns flow {
                    emit(Result.failure<List<User>>(exceptionInstance))
                }

                initViewModel()

                val result = userListViewModel.userList.first()

                Assertions.assertTrue(
                    result.isFailure && result.getOrNull()
                        .isNullOrEmpty() && result.exceptionOrNull() == exceptionInstance
                )
            }
    }

    @Nested
    inner class RefreshList {
        @Test
        fun `Try to refresh user ui model list`() = runBlocking {
            every {
                getUserListUseCase()
            } returns flow {
                emit(Result.success(fakeUserList))
            }

            every {
                refreshListUseCase()
            } returns flow {
                emit(Result.success(true))
            }

            initViewModel()

            val result = userListViewModel.refresh().first()
            Assertions.assertTrue(result.isSuccess && result.getOrNull() == true)
        }

        @ParameterizedTest
        @ValueSource(
            classes = [
                ApiException::class, NoInternetException::class, UnexpectedException::class
            ]
        )
        fun `Try to refresh user list model from usecase with an error`(
            exceptionClass: Class<Exception>
        ) =
            runBlocking {
                val exceptionInstance: Exception = generateExceptionFromClass(exceptionClass)

                every {
                    getUserListUseCase()
                } returns flow {
                    emit(Result.success(fakeUserList))
                }

                every {
                    refreshListUseCase()
                } returns flow { emit(Result.failure<Boolean>(exceptionInstance)) }

                initViewModel()

                val result = userListViewModel.refresh().first()
                Assertions.assertTrue(
                    result.isFailure && result.getOrNull() == null &&
                    result.exceptionOrNull() == exceptionInstance
                )
            }
    }

    private fun initViewModel() {
        userListViewModel = UserListViewModel(
            getUserListUseCase = getUserListUseCase,
            refreshListUseCase = refreshListUseCase
        )
    }
}
