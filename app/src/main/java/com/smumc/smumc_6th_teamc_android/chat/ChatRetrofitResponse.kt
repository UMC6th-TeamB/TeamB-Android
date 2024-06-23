package com.smumc.smumc_6th_teamc_android.chat

import com.google.gson.annotations.SerializedName

data class ChatRetrofitResponse(
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "message") val message: String,
    @SerializedName(value = "result") val result: String
)

data class ChatMessage(
    val sender: String,
    val content: String
)

data class ChatRoomRetrofitResult(
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "message") val message: String,
    @SerializedName(value = "result") val result: ArrayList<ChatRoom>
)
