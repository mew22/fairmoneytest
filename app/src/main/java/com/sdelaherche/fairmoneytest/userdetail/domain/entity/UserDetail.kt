package com.sdelaherche.fairmoneytest.userdetail.domain.entity

import com.sdelaherche.fairmoneytest.common.domain.entity.User
import java.util.*

data class UserDetail(
    val user: User,
    val gender: Gender,
    val email: EmailAddress,
    val birthDate: Date,
    val phone: PhoneNumber,
    val location: Location,
    val registerDate: Date,
    val updatedDate: Date
)