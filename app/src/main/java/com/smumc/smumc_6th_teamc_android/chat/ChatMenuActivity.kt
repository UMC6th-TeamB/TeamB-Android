package com.smumc.smumc_6th_teamc_android.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.smumc.smumc_6th_teamc_android.databinding.ActivityChatMenuBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatMenuBinding
    private lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMode = intent.getIntExtra("DISPLAY_MODE", 0)

        when (displayMode) {
            0 -> showNoMatchScreen()
            1 -> showChatScreen()
            else -> showNoMatchScreen()
        }

        setupChatMenuScreen()
        fetchChatRooms()
    }

    private fun showNoMatchScreen() {
        binding.noMatchLayout.visibility = ConstraintLayout.VISIBLE
        binding.chatRoomRecyclerView.visibility = ConstraintLayout.GONE
    }

    private fun showChatScreen() {
        binding.noMatchLayout.visibility = ConstraintLayout.GONE
        binding.chatRoomRecyclerView.visibility = ConstraintLayout.VISIBLE
    }

    private fun setupChatMenuScreen() {
        chatRoomAdapter = ChatRoomAdapter(emptyList()) { chatRoom ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("CHAT_ROOM_ID", chatRoom.id)
            startActivity(intent)
        }
        binding.chatRoomRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRoomRecyclerView.adapter = chatRoomAdapter
    }

    private fun fetchChatRooms() {
        RetrofitClient.instance.getChatRooms().enqueue(object : Callback<List<ChatRoom>> {
            override fun onResponse(call: Call<List<ChatRoom>>, response: Response<List<ChatRoom>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chatRoomAdapter.updateChatRooms(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<ChatRoom>>, t: Throwable) {
                // 에러 처리
            }
        })
    }
}
