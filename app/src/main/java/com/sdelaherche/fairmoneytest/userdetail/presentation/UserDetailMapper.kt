package com.sdelaherche.fairmoneytest.userdetail.presentation

import android.net.Uri
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.UserDetail
import com.sdelaherche.fairmoneytest.userdetail.presentation.Config.READABLE_DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.Locale

object Config {
    const val READABLE_DATE_PATTERN = "dd-MMM-yyyy HH:mm:ss"
}

fun UserDetail.toUi(): UserDetailModel = UserDetailModel(
    title = user.title.value,
    firstName = user.firstName.value,
    lastName = user.lastName.value,
    picture = Uri.parse(user.picture.toString()),
    gender = gender,
    email = email.value,
    birthDate = SimpleDateFormat(READABLE_DATE_PATTERN, Locale.getDefault()).format(birthDate),
    phone = phone.value,
    street = location.street.value,
    city = location.city.value,
    state = location.state.value,
    country = location.country.value,
    timezone = location.timezone.value
)
