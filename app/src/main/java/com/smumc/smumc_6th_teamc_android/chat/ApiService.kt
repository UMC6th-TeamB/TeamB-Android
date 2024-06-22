package com.smumc.smumc_6th_teamc_android.chat

import retrofit2.Call
import retrofit2.http.*

// ApiService 인터페이스 정의: Retrofit을 사용하여 API와 통신
interface ApiService {

    // 채팅방 목록을 가져오는 GET 요청
    @GET("chat-group")
    fun getChatRooms(): Call<List<ChatRoom>>

    // 특정 채팅방의 메시지를 가져오는 GET 요청
    @GET("chat-group/{chatGroupId}/join")
    fun getMessages(@Path("chatGroupId") chatGroupId: String): Call<List<Message>>

    // 특정 채팅방에 메시지를 전송하는 POST 요청
    @POST("chat-group/{chatGroupId}/send")
    fun sendMessage(@Path("chatGroupId") chatGroupId: String, @Body message: Message): Call<Void>

    // 특정 채팅방의 갱신된 메시지를 수신하는 GET 요청
    @GET("chat-group/{chatGroupId}/receive")
    fun receiveMessages(@Path("chatGroupId") chatGroupId: String): Call<List<Message>>
}
