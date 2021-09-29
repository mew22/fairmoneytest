package com.sdelaherche.fairmoneytest.userdetail.presentation

import android.net.Uri
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
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
import com.sdelaherche.fairmoneytest.userdetail.presentation.Config.READABLE_DATE_PATTERN
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class UserDetailMapperTest {

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
            birthDate = SimpleDateFormat(READABLE_DATE_PATTERN, Locale.getDefault()).format(
                birthdate
            ),
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
    inner class UserDetailEntityToUserDetailUiModel {
        @Test
        fun `Try to map an user detail entity to an user ui model`() {
            Assertions.assertEquals(fakeUserDetailModel, fakeUserDetail.toUi())
        }
    }
}