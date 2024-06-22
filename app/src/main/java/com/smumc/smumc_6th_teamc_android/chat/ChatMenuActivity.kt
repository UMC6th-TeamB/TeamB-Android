package com.smumc.smumc_6th_teamc_android.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.smumc.smumc_6th_teamc_android.databinding.ActivityChatMenuBinding

class ChatMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatMenuBinding
    private lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMode = intent.getIntExtra("DISPLAY_MODE", 0) // Intent로부터 DISPLAY_MODE 값을 가져옴

        when (displayMode) { // DISPLAY_MODE에 따라 화면을 변경
            0 -> showNoMatchScreen() // 매칭된 카풀이 없는 화면을 표시
            1 -> showChatScreen() // 채팅 화면을 표시
            else -> showNoMatchScreen() // 기본적으로 매칭된 카풀이 없는 화면을 표시
        }

        setupChatMenuScreen()
    }


    private fun showNoMatchScreen() {
        binding.noMatchLayout.visibility = ConstraintLayout.VISIBLE // 매칭된 카풀이 없는 레이아웃을 표시
        binding.chatRoomRecyclerView.visibility = ConstraintLayout.GONE // 채팅 레이아웃을 숨김
    }

    private fun showChatScreen() {
        binding.noMatchLayout.visibility = ConstraintLayout.GONE // 매칭된 카풀이 없는 레이아웃을 숨김
        binding.chatRoomRecyclerView.visibility = ConstraintLayout.VISIBLE // 채팅 레이아웃을 표시

    }

    private fun setupChatMenuScreen() {

        val members = listOf(
            Member("유지민", "@drawable/ic_user_avatar", "19학번", "컴퓨터과학과"),
            Member("홍길동", "@drawable/ic_user_avatar", "19학번", "컴퓨터과학과"),
            Member("박심청", "@drawable/ic_user_avatar", "19학번", "컴퓨터과학과"),
            Member("이소희", "@drawable/ic_user_avatar", "19학번", "컴퓨터과학과"),
        )

        val chatRooms = listOf(
            ChatRoom("24.05.27 시청역", "3", "안녕하세요 스뮤플...", "오후 4:13", members),
            ChatRoom("24.05.24 시청역", "2", "안녕하세요 스뮤플...", "오후 2:23", members),
            ChatRoom("24.05.20 시청역", "2", "안녕하세요 스뮤플...", "오후 12:20", members)
        )

        chatRoomAdapter = ChatRoomAdapter(chatRooms) { chatRoom ->
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        binding.chatRoomRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRoomRecyclerView.adapter = chatRoomAdapter
    }

//    private fun ChatRoom(date: String, region: String, message: String, time: String, members: List<Member>): ChatRoom {
//
//    }
}
