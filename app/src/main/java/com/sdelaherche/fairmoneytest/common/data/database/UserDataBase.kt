package com.sdelaherche.fairmoneytest.common.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.sdelaherche.fairmoneytest.common.data.database.UserDataBase.Companion.DATABASE_VERSION
import com.sdelaherche.fairmoneytest.common.data.local.IUserLocalDataSource
import com.sdelaherche.fairmoneytest.common.data.model.UserData
import java.util.Date

@Database(
    entities = [
        UserData::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class
)
abstract class UserDataBase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "fairmoney"
        const val DATABASE_VERSION = 1
    }

    abstract val userDao: IUserLocalDataSource
}

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}
