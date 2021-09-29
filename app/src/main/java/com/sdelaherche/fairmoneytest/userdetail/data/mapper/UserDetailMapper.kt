package com.sdelaherche.fairmoneytest.userdetail.data.mapper

import com.sdelaherche.fairmoneytest.common.data.mapper.toEntity
import com.sdelaherche.fairmoneytest.common.data.model.UserData
import com.sdelaherche.fairmoneytest.common.data.model.UserDetailData
import com.sdelaherche.fairmoneytest.userdetail.data.remote.UserDetailRemoteData
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.EmailAddress
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Gender
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.PhoneNumber
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.UserDetail

fun UserDetailRemoteData.toLocal() = UserData(
    id = id,
    title = title,
    firstName = firstName,
    lastName = lastName,
    picture = picture,
    detail = UserDetailData(
        gender = gender,
        email = email,
        birthDate = birthdate,
        phone = phone,
        location = location.toLocal(),
        registerDate = registerDate,
        updatedDate = updatedDate
    )
)

fun UserData.toEntity(): UserDetail? =
    detail?.let {
        UserDetail(
            user = toEntity(),
            gender = Gender.valueOf(it.gender.uppercase()),
            email = EmailAddress(it.email),
            phone = PhoneNumber(it.phone),
            location = it.location.toEntity(),
            birthDate = it.birthDate,
            registerDate = it.registerDate,
            updatedDate = it.updatedDate
        )
    }
