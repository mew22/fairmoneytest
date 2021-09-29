package com.sdelaherche.fairmoneytest.userlist.presentation

import android.net.Uri

data class UserModel(
    val id: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val picture: Uri
)
