package com.sdelaherche.fairmoneytest.userlist.data.remote

import com.google.gson.annotations.SerializedName

data class UserRemoteData(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("picture") val picture: String
)
