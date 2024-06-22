package com.smumc.smumc_6th_teamc_android.login.api

import com.google.gson.annotations.SerializedName

data class UserRetrofitResponse (
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "message") val message: String,
    @SerializedName(value = "result") val result: String
)

data class UserResult(
    @SerializedName(value = "Id") val Id: Int,
    @SerializedName(value = "nickName") val nickName: String,
    @SerializedName(value = "accessToken") val accessToken: String,
    @SerializedName(value = "refreshToken") val refreshToken: String
)