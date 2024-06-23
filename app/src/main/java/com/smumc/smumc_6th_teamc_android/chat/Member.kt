// src/main/java/com/smumc/smumc_6th_teamc_android/chat/Member.kt
package com.smumc.smumc_6th_teamc_android.chat

// 멤버 데이터를 나타내는 데이터 클래스
data class Member(
    val name: String, // 멤버의 이름
    val img: String, // 멤버의 이미지 경로
    val year: String, // 멤버의 학번
    val department: String // 멤버의 학과
)
