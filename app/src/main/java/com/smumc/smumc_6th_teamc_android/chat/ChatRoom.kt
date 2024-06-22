// src/main/java/com/smumc/smumc_6th_teamc_android/chat/ChatRoom.kt
package com.smumc.smumc_6th_teamc_android.chat

// 채팅방 데이터를 나타내는 데이터 클래스
data class ChatRoom(
    val id: String, // 채팅방의 고유 식별자
    val date: String, // 채팅방의 날짜
    val region: String, // 채팅방의 지역
    val message: String, // 채팅방의 메시지
    val time: String, // 채팅방의 시간
    val members: List<Member> // 채팅방의 멤버 리스트
)
