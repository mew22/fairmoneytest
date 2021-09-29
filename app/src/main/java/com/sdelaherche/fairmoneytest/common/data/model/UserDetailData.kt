package com.sdelaherche.fairmoneytest.common.data.model

import androidx.room.Embedded
import java.util.Date

data class UserDetailData(
    val gender: String,
    val email: String,
    val birthDate: Date,
    val phone: String,
    @Embedded val location: LocationData,
    val registerDate: Date,
    val updatedDate: Date
)
