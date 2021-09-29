package com.sdelaherche.fairmoneytest.common.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserData.TABLE_NAME)
data class UserData(
    @PrimaryKey val id: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val picture: String,
    @Embedded val detail: UserDetailData?
) {
    companion object {
        const val TABLE_NAME = "UserData"
    }
}
