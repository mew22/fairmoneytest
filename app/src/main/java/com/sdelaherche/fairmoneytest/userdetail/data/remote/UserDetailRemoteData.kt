package com.sdelaherche.fairmoneytest.userdetail.data.remote

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserDetailRemoteData(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("picture") val picture: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("email") val email: String,
    @SerializedName("dateOfBirth") val birthdate: Date,
    @SerializedName("phone") val phone: String,
    @SerializedName("location") val location: LocationRemoteData,
    @SerializedName("registerDate") val registerDate: Date,
    @SerializedName("updatedDate") val updatedDate: Date,
)
