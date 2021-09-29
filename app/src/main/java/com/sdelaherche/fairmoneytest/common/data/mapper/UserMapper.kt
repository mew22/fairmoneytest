package com.sdelaherche.fairmoneytest.common.data.mapper

import com.sdelaherche.fairmoneytest.common.data.model.UserData
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.entity.Name
import com.sdelaherche.fairmoneytest.common.domain.entity.Title
import com.sdelaherche.fairmoneytest.common.domain.entity.User
import java.net.URI

fun UserData.toEntity(): User = User(
    id = Id(id),
    title = Title(title),
    firstName = Name(firstName),
    lastName = Name(lastName),
    picture = URI.create(picture)
)

fun List<UserData>.toEntity(): List<User> = map { it.toEntity() }
