package com.sdelaherche.fairmoneytest.userlist.data.mapper

import com.sdelaherche.fairmoneytest.common.data.model.UserData
import com.sdelaherche.fairmoneytest.userlist.data.remote.UserRemoteData

fun UserRemoteData.toLocal(): UserData = UserData(
    id = id,
    title = title,
    firstName = firstName,
    lastName = lastName,
    picture = picture,
    detail = null
)

fun List<UserRemoteData>.toLocal(): List<UserData> = map { it.toLocal() }
