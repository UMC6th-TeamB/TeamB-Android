// src/main/java/com/smumc/smumc_6th_teamc_android/chat/ChatRoom.kt
package com.smumc.smumc_6th_teamc_android.chat

data class ChatRoom(
    val date: String,
    val region: String,
    val message: String,
    val time: String,
    val members: List<Member>
)
