// src/main/java/com/smumc/smumc_6th_teamc_android/chat/ChatRoom.kt
package com.smumc.smumc_6th_teamc_android.chat

import com.google.gson.annotations.SerializedName

// 채팅방 데이터를 나타내는 데이터 클래스
data class ChatRoom(
    @SerializedName(value = "chatRoomId") val chatRoomId: Int, // 채팅방의 고유 식별자
    @SerializedName(value = "dateTime") val dateTime: String, // 채팅방의 날짜
    @SerializedName(value = "region") val region: String, // 채팅방의 지역
    @SerializedName(value = "lastMessage") val lastMessage: String, // 채팅방의 메시지
    @SerializedName(value = "memberCount") val memberCount: Int // 채팅방의 멤버 리스트
)
