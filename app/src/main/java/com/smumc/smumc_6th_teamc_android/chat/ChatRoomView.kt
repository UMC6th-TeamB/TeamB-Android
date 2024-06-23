package com.smumc.smumc_6th_teamc_android.chat

interface ChatRoomView {
    fun onGetChatRoomLoading()
    fun onGetChatRoomSuccess(result: ArrayList<ChatRoom>)
    fun onGetChatRoomFailure(errorCode: Int, errorMessage: String)
}