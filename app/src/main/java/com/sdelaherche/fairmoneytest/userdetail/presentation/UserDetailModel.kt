package com.sdelaherche.fairmoneytest.userdetail.presentation

import android.net.Uri
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Gender

data class UserDetailModel(
    val title: String,
    val firstName: String,
    val lastName: String,
    val picture: Uri,
    val gender: Gender,
    val email: String,
    val birthDate: String,
    val phone: String,
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val timezone: String
)
