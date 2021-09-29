package com.sdelaherche.fairmoneytest.userlist.data.remote

import com.google.gson.annotations.SerializedName

data class ResponseRemoteData(
    @SerializedName("data") val data: List<UserRemoteData>,
    @SerializedName("page") val page: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("limit") val limit: Int
)
