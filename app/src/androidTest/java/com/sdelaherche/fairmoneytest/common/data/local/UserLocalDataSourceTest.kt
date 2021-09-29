package com.sdelaherche.fairmoneytest.common.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdelaherche.fairmoneytest.common.data.database.UserDataBase
import com.sdelaherche.fairmoneytest.common.data.model.LocationData
import com.sdelaherche.fairmoneytest.common.data.model.UserData
import com.sdelaherche.fairmoneytest.common.data.model.UserDetailData
import com.sdelaherche.fairmoneytest.util.CoroutineTestRule
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserLocalDataSourceTest {
    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var userDataBase: UserDataBase

    private lateinit var userDao: IUserLocalDataSource

    private val birthdate = Date()
    private val registerDate = Date()
    private val updatedDate = Date()
    private val fakeUserDataList = mutableListOf<UserData>().apply {
        for (i in 0 until 10) {
            add(
                UserData(
                    id = "id$i", title = "title", firstName = "firstName",
                    lastName = "lastName", picture = "http://test.com",
                    detail = null
                )
            )
        }
    }

    private val fakeUserData = UserData(
        id = "id0", title = "title", firstName = "firstName",
        lastName = "lastName", picture = "http://test.com",
        detail = UserDetailData(
            gender = "male",
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

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        userDataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDataBase::class.java
        )
            .setTransactionExecutor(testDispatcher.asExecutor())
            .setQueryExecutor(testDispatcher.asExecutor())
            .build()
        userDao = userDataBase.userDao
    }

    @ExperimentalCoroutinesApi
    @After
    fun closeDb() {
        userDataBase.close()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun tryGetUserListAfterInsertList() = coroutinesTestRule.testDispatcher.runBlockingTest {
        assert(!userDao.insertUserList(fakeUserDataList.subList(0, 5)).isNullOrEmpty())
        assert(userDao.getUsers().first() == fakeUserDataList.subList(0, 5))
        assert(!userDao.insertUserList(fakeUserDataList.subList(5, 10)).isNullOrEmpty())
        assert(userDao.getUsers().first() == fakeUserDataList)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun tryGetUserByIdAfterInsertList() = coroutinesTestRule.testDispatcher.runBlockingTest {
        assert(!userDao.insertUserList(fakeUserDataList).isNullOrEmpty())
        assert(userDao.getUserById(id = fakeUserDataList[4].id).first() == fakeUserDataList[4])
    }

    @ExperimentalCoroutinesApi
    @Test
    fun tryUpdateUserAfterInsertListConflictReplace() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            assert(!userDao.insertUserList(fakeUserDataList).isNullOrEmpty())
            assert(userDao.updateUser(fakeUserData) == 1)
            assert(userDao.getUserById(id = fakeUserData.id).first() == fakeUserData)
        }

    @ExperimentalCoroutinesApi
    @Test
    fun tryInsertUserAfterInsertListConflictIgnore() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            assert(!userDao.insertUserList(fakeUserDataList).isNullOrEmpty())
            assert(userDao.insertUser(fakeUserData) != 0L)
            assert(userDao.getUsers().first() == fakeUserDataList)
        }
}
