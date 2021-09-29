package com.sdelaherche.fairmoneytest.common.data.mapper

import com.sdelaherche.fairmoneytest.common.data.model.UserData
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import java.net.URI
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserMapperTest {

    private val fakeInitialUserData = UserData(
        id = "id", title = "title", firstName = "firstName",
        lastName = "lastName", picture = "http://test.com",
        detail = null
    )

    private val fakeUser = User(
        id = Id("id"), title = Title("title"), firstName = Name("firstName"),
        lastName = Name("lastName"), picture = URI.create("http://test.com")
    )

    @Nested
    inner class UserLocalToEntity {
        @Test
        fun `Try to map an user data object to an user entity`() {
            Assertions.assertEquals(fakeUser, fakeInitialUserData.toEntity())
        }

        @Test
        fun `Try to map a list of user data object to a list of user entity`() {
            Assertions.assertEquals(listOf(fakeUser), listOf(fakeInitialUserData).toEntity())
        }
    }
}
