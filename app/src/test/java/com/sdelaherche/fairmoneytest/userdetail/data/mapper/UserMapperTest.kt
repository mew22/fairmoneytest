package com.sdelaherche.fairmoneytest.userdetail.data.mapper

import com.sdelaherche.fairmoneytest.common.data.model.LocationData
import com.sdelaherche.fairmoneytest.common.data.model.UserData
import com.sdelaherche.fairmoneytest.common.data.model.UserDetailData
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import com.sdelaherche.fairmoneytest.userdetail.data.remote.LocationRemoteData
import com.sdelaherche.fairmoneytest.userdetail.data.remote.UserDetailRemoteData
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.*
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.TimeZone
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URI
import java.util.*

class UserMapperTest {

    private val birthdate = Date()
    private val registerDate = Date()
    private val updatedDate = Date()

    private val fakeInitialUserData = UserData(
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

    private val fakeUserRemoteData = UserDetailRemoteData(
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

    @Nested
    inner class UserDetailRemoteToUserLocal {
        @Test
        fun `Try to map an user detail remote data object to an user detail local data`() {
            Assertions.assertEquals(fakeUserData, fakeUserRemoteData.toLocal())
        }
    }

    @Nested
    inner class UserLocalToUserDetailEntity {
        @Test
        fun `Try to map an user data object to an user detail entity`() {
            Assertions.assertEquals(fakeUserDetail, fakeUserData.toEntity())
        }

        @Test
        fun `Try to map a wrong user data object to an user detail entity and return null`() {
            Assertions.assertNull(fakeInitialUserData.toEntity())
        }
    }
}