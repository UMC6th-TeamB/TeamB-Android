package com.smumc.smumc_6th_teamc_android.chat

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

object WebSocketManager {
    private lateinit var webSocketClient: WebSocketClient
    private lateinit var chatRVAdapter: ChatRVAdapter

    fun connectWebSocket(serverUri: URI, adapter: ChatRVAdapter) {
        chatRVAdapter = adapter
        webSocketClient = object : WebSocketClient(serverUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                // 웹 소켓이 열렸을 때 처리
                println("WebSocket Opened: $serverUri")
            }

            override fun onMessage(message: String?) {
                message?.let {
                    // 메시지를 처리하고 어댑터에 추가
                    val chatMessage = Message(it, "상대방", false) // 예시로 사용자가 "상대방"으로 설정
                    chatRVAdapter.addMessage(chatMessage)
                    println("Received message: $it")
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                // 웹 소켓이 닫혔을 때 처리
                println("WebSocket Closed. Code: $code, Reason: $reason, Remote: $remote")
            }

            override fun onError(ex: Exception?) {
                // 오류가 발생했을 때 처리
                ex?.printStackTrace()
            }
        }
        webSocketClient.connect()
    }

    fun sendMessage(message: String) {
        webSocketClient.send(message)
    }
}