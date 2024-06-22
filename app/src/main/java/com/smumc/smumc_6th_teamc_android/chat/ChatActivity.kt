package com.smumc.smumc_6th_teamc_android.chat

import android.os.Bundle
import android.util.Log // 로그 출력을 위한 Log 클래스 임포트
import android.widget.Toast // 사용자에게 메시지를 표시하기 위한 Toast 클래스 임포트
import androidx.appcompat.app.AppCompatActivity // AppCompatActivity 상속
import androidx.recyclerview.widget.LinearLayoutManager // RecyclerView의 LayoutManager 설정을 위한 임포트
import com.smumc.smumc_6th_teamc_android.databinding.ActivityChatBinding
import retrofit2.Call // Retrofit의 Call 클래스 임포트
import retrofit2.Callback // Retrofit의 Callback 클래스 임포트
import retrofit2.Response // Retrofit의 Response 클래스 임포트
import java.net.URI // URI 클래스 임포트
import java.util.* // Timer 클래스 임포트
import kotlin.concurrent.fixedRateTimer // 일정 간격으로 타이머 실행을 위한 임포트

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding // View Binding을 사용하기 위한 변수
    private lateinit var chatRVAdapter: ChatRVAdapter // RecyclerView 어댑터를 위한 변수
    private lateinit var chatRoomId: String // 채팅방 ID를 저장하기 위한 변수
    private lateinit var timer: Timer // 메시지 수신 타이머를 위한 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater) // View Binding 초기화
        setContentView(binding.root) // 레이아웃 설정

        setSupportActionBar(binding.toolbar) // 툴바 설정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바 타이틀 비활성화

        chatRoomId = intent.getStringExtra("CHAT_ROOM_ID") ?: "" // Intent로부터 채팅방 ID를 가져옴

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // 네비게이션 버튼 클릭 시 뒤로 가기
        }

        setupChatScreen() // 채팅 화면 설정
        fetchMessages() // 초기 메시지 로드
        connectWebSocket() // 웹소켓 연결
        startReceivingMessages() // 주기적인 메시지 수신 시작

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString() // 입력된 메시지 텍스트 가져오기
            if (messageText.isNotBlank()) { // 메시지가 공백이 아닌 경우
                val message = Message(messageText, "나", true) // 메시지 객체 생성
                chatRVAdapter.addMessage(message) // 어댑터에 메시지 추가
                binding.messageInput.text.clear() // 입력 필드 비우기
                binding.recyclerView.scrollToPosition(chatRVAdapter.itemCount - 1) // RecyclerView를 스크롤하여 마지막 메시지 표시
                sendMessageToServer(message) // 서버로 메시지 전송
            }
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = binding.root.rootView.height - binding.root.height
            if (heightDiff > dpToPx(200)) { // 키보드가 나타난 경우
                binding.recyclerView.post {
                    binding.recyclerView.scrollToPosition((binding.recyclerView.adapter?.itemCount ?: 0) - 1)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopReceivingMessages() // 액티비티가 파괴될 때 메시지 수신 중지
    }

    private fun setupChatScreen() {
        chatRVAdapter = ChatRVAdapter(mutableListOf()) // 어댑터 초기화
        binding.recyclerView.layoutManager = LinearLayoutManager(this) // 레이아웃 매니저 설정
        binding.recyclerView.adapter = chatRVAdapter // 어댑터 설정
    }

    private fun fetchMessages() {
        RetrofitClient.instance.getMessages(chatRoomId).enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chatRVAdapter.updateMessages(it) // 메시지 리스트 업데이트
                    }
                } else {
                    // 응답은 받았지만 성공적이지 않을 경우의 처리
                    showError("Failed to fetch messages: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                // 에러 처리
                showError("Failed to fetch messages: ${t.message}")
            }
        })
    }

    private fun sendMessageToServer(message: Message) {
        RetrofitClient.instance.sendMessage(chatRoomId, message).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // 메시지 전송 성공 처리
                    Log.d("ChatActivity", "Message sent successfully: ${message.text}")
                } else {
                    // 응답은 받았지만 성공적이지 않을 경우의 처리
                    showError("Failed to send message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 에러 처리
                showError("Failed to send message: ${t.message}")
            }
        })
    }

    private fun connectWebSocket() {
        val serverUri = URI("wss://yourbackend.com/ws/chat/$chatRoomId") // 웹소켓 URI 설정
        WebSocketManager.connectWebSocket(serverUri, chatRVAdapter) // 웹소켓 연결
    }

    private fun startReceivingMessages() {
        timer = fixedRateTimer("receiveMessagesTimer", initialDelay = 0, period = 5000) { // 일정 간격으로 메시지 수신
            RetrofitClient.instance.receiveMessages(chatRoomId).enqueue(object : Callback<List<Message>> {
                override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            chatRVAdapter.updateMessages(it) // 메시지 리스트 업데이트
                        }
                    } else {
                        // 응답은 받았지만 성공적이지 않을 경우의 처리
                        showError("Failed to receive messages: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                    // 에러 처리
                    showError("Failed to receive messages: ${t.message}")
                }
            })
        }
    }

    private fun showError(message: String?) {
        // 오류 메시지를 사용자에게 표시하는 함수
        // 예: 토스트 메시지, 다이얼로그 등
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun stopReceivingMessages() {
        timer.cancel() // 타이머 중지
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density // 디스플레이 밀도 가져오기
        return (dp * density).toInt() // dp 값을 px 값으로 변환
    }
}
