package com.sdelaherche.fairmoneytest.common.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sdelaherche.fairmoneytest.common.data.model.UserData
import kotlinx.coroutines.flow.Flow

@Dao
interface IUserLocalDataSource {
    @Query("SELECT * FROM ${UserData.TABLE_NAME}")
    fun getUsers(): Flow<List<UserData>>

    @Query("SELECT * FROM ${UserData.TABLE_NAME} WHERE id = :id")
    fun getUserById(id: String): Flow<UserData?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(data: UserData): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserList(data: List<UserData>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(data: UserData): Int
}
