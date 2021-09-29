package com.sdelaherche.fairmoneytest.userlist.presentation

import android.net.Uri
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.*
import java.net.URI

class UserMapperTest {

    @MockK
    private lateinit var userUri: Uri

    private val fakeUser by lazy {
        User(
            id = Id("id"), title = Title("title"), firstName = Name("firstName"),
            lastName = Name("lastName"), picture = URI.create("http://test.com")
        )
    }

    private val fakeUserUiModel by lazy {
        UserModel(
            id = "id", title = "title", firstName = "firstName",
            lastName = "lastName", picture = Uri.parse("http://test.com")
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
    inner class UserEntityToUiModel {
        @Test
        fun `Try to map an user entity to an user ui model`() {
            Assertions.assertEquals(fakeUserUiModel, fakeUser.toUi())
        }

        @Test
        fun `Try to map a list of user entity to a list of user ui model`() {
            Assertions.assertEquals(listOf(fakeUserUiModel), listOf(fakeUser).toUi())
        }
    }
}