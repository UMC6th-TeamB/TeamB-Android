package com.smumc.smumc_6th_teamc_android.login.api

import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName(value = "studentId") var studentId : String
)

data class CertificationNum(
    @SerializedName(value = "certificationNum") var certificationNum : String
)

data class Nickname(
    @SerializedName(value = "nickname") var nickname: String
)
