package com.smumc.smumc_6th_teamc_android.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.smumc.smumc_6th_teamc_android.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding // View Binding을 사용하여 레이아웃의 뷰 요소에 접근하기 위한 변수
    private lateinit var chatRVAdapter: ChatRVAdapter // RecyclerView 어댑터를 위한 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // 슈퍼클래스의 onCreate 메서드 호출
        binding = ActivityChatBinding.inflate(layoutInflater) // View Binding 초기화
        setContentView(binding.root) // 뷰 설정

        setSupportActionBar(binding.toolbar) // 툴바 설정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바 타이틀 비활성화

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // 네비게이션 버튼 클릭 시 뒤로가기 동작 설정
        }

        setupChatScreen() // 채팅 화면 설정

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString() // EditText에서 텍스트를 가져옴
            if (messageText.isNotBlank()) { // 메시지가 공백이 아닌지 확인
                val message = Message(messageText, "나", true) // 메시지 객체 생성
                chatRVAdapter.addMessage(message) // 메시지를 어댑터에 추가
                binding.messageInput.text.clear() // EditText를 비움
                binding.recyclerView.scrollToPosition(chatRVAdapter.itemCount - 1) // RecyclerView를 스크롤하여 마지막 메시지를 표시
            }
        }

        //키보드 입력시
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = binding.root.rootView.height - binding.root.height // 화면 높이 변화 계산 : 키보드가 나타나기 전의 전체 화면 높이 - 키보드가 나타난 후의 현재 루트 뷰의 높이
            if (heightDiff > dpToPx(200)) { // 임계값을 적절하게 조정 (화면의 높이 변화(heightDiff)가 200dp보다 클 경우, 키보드가 나타났다고 간주)
                binding.recyclerView.post {
                    binding.recyclerView.scrollToPosition((binding.recyclerView.adapter?.itemCount ?: 0) - 1) // RecyclerView를 스크롤하여 마지막 메시지를 표시
                }
            }
        }
    }

    private fun setupChatScreen() {
        val messages = mutableListOf( // 초기 메시지 리스트를 생성
            Message("안녕하세요\n스뮤플 사용자1 입니다.", "", false),
            Message("안녕하세요\n스뮤플 사용자2 입니다.", "", false),
            Message("안녕하세요\n스뮤플 사용자3 입니다.", "", false),
            Message("안녕하세요\n스뮤플 사용자4 입니다.", "", false),
            Message("안녕하세요\n스뮤플 사용자5 입니다.", "", true)
        )

        chatRVAdapter = ChatRVAdapter(messages) // RecyclerView 어댑터를 초기화
        binding.recyclerView.layoutManager = LinearLayoutManager(this) // RecyclerView 레이아웃 매니저 설정
        binding.recyclerView.adapter = chatRVAdapter // RecyclerView 어댑터 설정
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density // 디스플레이 밀도 가져오기
        return (dp * density).toInt() // dp 값을 px 값으로 변환
    }
}
