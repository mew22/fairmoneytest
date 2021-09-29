package com.sdelaherche.fairmoneytest.userlist.presentation

import android.net.Uri
import com.sdelaherche.fairmoneytest.common.domain.entity.User

fun User.toUi(): UserModel = UserModel(
    id = id.value,
    title = title.value,
    firstName = firstName.value,
    lastName = lastName.value,
    picture = Uri.parse(picture.toString())
)

fun List<User>.toUi(): List<UserModel> = map {
    it.toUi()
}
