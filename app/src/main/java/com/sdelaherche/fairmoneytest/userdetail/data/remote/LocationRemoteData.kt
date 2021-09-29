package com.sdelaherche.fairmoneytest.userdetail.data.remote

import com.google.gson.annotations.SerializedName

data class LocationRemoteData(
    @SerializedName("street") val street: String,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
    @SerializedName("timezone") val timezone: String,
)
