package com.smumc.smumc_6th_teamc_android.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.smumc.smumc_6th_teamc_android.databinding.ActivityChatMenuBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatMenuActivity : AppCompatActivity(), ChatRoomView {
    companion object {
        private const val TAG = "ChatMenuTest"
    }

    private lateinit var binding: ActivityChatMenuBinding
    private lateinit var chatRoomAdapter: ChatRoomRVAdapter
    private var BEARER_TOKEN: String? = "" // 로그인 토큰 값 전달 받는 변수 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //MapActivity에서 토큰 값 전달 받음
        BEARER_TOKEN = intent.getStringExtra("BearerToken")

        // Bearer 제거
        BEARER_TOKEN = BEARER_TOKEN?.substring(8).toString()
        Log.d("MYPAGE 토큰 값", BEARER_TOKEN.toString())

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if(BEARER_TOKEN != null){
            showChatScreen()
        } else {
            showNoMatchScreen()
        }

        getChatRooms()
    }

    private fun showNoMatchScreen() {
        binding.noMatchLayout.visibility = ConstraintLayout.VISIBLE
        binding.chatRoomRecyclerView.visibility = ConstraintLayout.GONE
    }

    private fun showChatScreen() {
        binding.noMatchLayout.visibility = ConstraintLayout.GONE
        binding.chatRoomRecyclerView.visibility = ConstraintLayout.VISIBLE
    }

    private fun setupChatMenuScreen(chatRooms: ArrayList<ChatRoom>) {
        chatRoomAdapter = ChatRoomRVAdapter(chatRooms) { chatRoom ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("CHAT_ROOM_ID", chatRoom.chatRoomId.toString())
            startActivity(intent)
        }
        binding.chatRoomRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRoomRecyclerView.adapter = chatRoomAdapter
    }

    private fun getChatRooms() {
        val chatRoomService = RestClient.getRetrofit().create(ChatRetrofitInterfaces::class.java)
        onGetChatRoomLoading()
        chatRoomService.getChatRooms("Bearer $BEARER_TOKEN", "4").enqueue(object :
            Callback<ChatRoomRetrofitResult> {
            override fun onResponse(call: Call<ChatRoomRetrofitResult>, response: Response<ChatRoomRetrofitResult>) {
                if (response.isSuccessful) {
                    Log.d("CHAT/SUCCESS", response.toString())
                    val resp: ChatRoomRetrofitResult? = response.body()
                    if (resp != null) {
                        if (resp.isSuccess) {
                            Log.d("CHAT/SUCCESS", "채팅방 로딩 성공: ${resp.result}")
                            onGetChatRoomSuccess(resp.result)
                        } else {
                            onGetChatRoomFailure(400, "Server Error: ${resp.code}")
                        }
                    } else {
                        onGetChatRoomFailure(400, "네트워크 오류가 발생했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<ChatRoomRetrofitResult>, t: Throwable) {
                Log.e("CHAT/FAILURE", t.message.toString())
                toast(t.message ?: "Unknown error")
            }
        })
    }

    private fun toast(text: String) {
        Log.i(TAG, text)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onGetChatRoomLoading() {

    }

    override fun onGetChatRoomSuccess(result: ArrayList<ChatRoom>) {
        setupChatMenuScreen(result)
    }

    override fun onGetChatRoomFailure(errorCode: Int, errorMessage: String) {
        Log.d("LOOK-FRAG/CHATROOM-RESPONSE", errorMessage)
    }
}
