package com.smumc.smumc_6th_teamc_android.chat

import retrofit2.Call
import retrofit2.http.*

interface ChatRetrofitInterfaces {

    // 채팅방 목록을 가져오는 GET 요청
    @GET("chat-group/{userId}")
    fun getChatRooms(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): Call<ChatRoomRetrofitResult>

    // 특정 채팅방의 메시지를 가져오는 GET 요청
    @GET("chat-group/{chatGroupId}/join")
    fun getMessages(@Path("chatGroupId") chatGroupId: String): Call<List<Message>>

    // 특정 채팅방에 메시지를 전송하는 POST 요청
    @POST("/sub/chat.message/{chatGroupId}")
    fun sendRestEcho(
        @Path("chatGroupId") chatGroupId: String,
        @Body chatMessage: ChatMessage
    ): Call<ChatRetrofitResponse>

    // 특정 채팅방에 메시지를 전송하는 POST 요청
    @POST("chat-group/{chatGroupId}/send")
    fun sendMessage(@Path("chatGroupId") chatGroupId: String, @Body message: Message): Call<Void>

    // 특정 채팅방의 갱신된 메시지를 수신하는 GET 요청
    @GET("chat-group/{chatGroupId}/receive")
    fun receiveMessages(@Path("chatGroupId") chatGroupId: String): Call<List<Message>>
}
