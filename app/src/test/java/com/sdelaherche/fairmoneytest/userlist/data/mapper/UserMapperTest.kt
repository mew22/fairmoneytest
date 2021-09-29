package com.sdelaherche.fairmoneytest.userlist.data.mapper

import com.sdelaherche.fairmoneytest.common.data.model.UserData
import com.sdelaherche.fairmoneytest.userlist.data.remote.UserRemoteData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserMapperTest {

    private val fakeUserData = UserData(
        id = "id", title = "title", firstName = "firstName",
        lastName = "lastName", picture = "http://test.com",
        detail = null
    )

    private val fakeUserRemoteData = UserRemoteData(
        id = "id", title = "title", firstName = "firstName",
        lastName = "lastName", picture = "http://test.com"
    )

    @Nested
    inner class UserRemoteToUserLocal {
        @Test
        fun `Try to map an user remote data object to an user local data`() {
            Assertions.assertEquals(fakeUserData, fakeUserRemoteData.toLocal())
        }

        @Test
        fun `Try to map a list of user remote data object to a list of user local data`() {
            Assertions.assertEquals(listOf(fakeUserData), listOf(fakeUserRemoteData).toLocal())
        }
    }
}