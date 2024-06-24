package com.smumc.smumc_6th_teamc_android.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.smumc.smumc_6th_teamc_android.databinding.ActivityChatBinding
import io.reactivex.CompletableTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ChatActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ChatA"
    }

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatRVAdapter: ChatRVAdapter
    private lateinit var mStompClient: StompClient
    private val mTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val mGson: Gson = GsonBuilder().create()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var chatRoomId: String
    private var BEARER_TOKEN: String? = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        chatRVAdapter = ChatRVAdapter(mutableListOf())
        chatRVAdapter.setHasStableIds(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = chatRVAdapter

        //MapActivity에서 토큰 값 전달 받음
        BEARER_TOKEN = intent.getStringExtra("BearerToken")

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws")

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        chatRoomId = intent.getStringExtra("CHAT_ROOM_ID") ?: ""
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.title.text = intent.getStringExtra("Title")

        setupChatScreen()
        connectStomp()

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString()
            if (messageText.isNotBlank()) {
                val message = Message(messageText, "${BEARER_TOKEN}", true, mTimeFormat.format(Date()))
                chatRVAdapter.addMessage(message)
                binding.messageInput.text.clear()
                binding.recyclerView.scrollToPosition(chatRVAdapter.itemCount - 1)
//                sendRestEcho(message)
                Log.d(TAG, "token ${BEARER_TOKEN.toString()}")
                sendEchoViaStomp(messageText, "${BEARER_TOKEN}")
            }
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = binding.root.rootView.height - binding.root.height
            if (heightDiff > dpToPx(200)) {
                binding.recyclerView.post {
                    binding.recyclerView.scrollToPosition((binding.recyclerView.adapter?.itemCount ?: 0) - 1)
                }
            }
        }

        val dispPing = mStompClient.topic("/sub/chat.message/$chatRoomId").toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message ->
                Log.d(TAG, "Received ${message.payload}")
                val echoModel = mGson.fromJson(message.payload, EchoModel::class.java)
                if (echoModel.content != null) {
                    addItem(echoModel)
                }
            }, { throwable ->
                Log.e(TAG, "Error on subscribe topic", throwable)
            })

        compositeDisposable.add(dispPing)
    }

    fun disconnectStomp() {
        mStompClient.disconnect()
        compositeDisposable.clear()
    }

    fun connectStomp() {
        val headers = listOf(
            StompHeader("Bearer", BEARER_TOKEN)
        )
        resetSubscriptions()
        mStompClient.connect(headers)

        val dispLifecycle = mStompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { event ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> {
//                        toast("Stomp connection opened")
                        subscribeToTopic()
                    }
                    LifecycleEvent.Type.ERROR -> {
                        Log.e(TAG, "Stomp connection error", event.exception)
//                        toast("Stomp connection error")
                    }
                    LifecycleEvent.Type.CLOSED -> {
//                        toast("Stomp connection closed")
                        resetSubscriptions()
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT ->
                        Log.e(TAG, "Stomp failed server heartbeat")
//                        toast("Stomp failed server heartbeat")
                }
            }

        compositeDisposable.add(dispLifecycle)
    }

    private fun sendEchoViaStomp(message: String, sender: String) {
        val gson = Gson()
        val data = mapOf("sender" to sender, "content" to message)
        val jsonData = gson.toJson(data)
        compositeDisposable.add(mStompClient.send("/sub/chat.message/$chatRoomId", jsonData)
            .compose(applySchedulers())
            .subscribe({
                Log.d(TAG, "STOMP echo send successfully")
            }, { throwable ->
                Log.e(TAG, "Error send STOMP echo", throwable)
//                toast(throwable.message ?: "Unknown error")
            }))
    }

//    private fun sendRestEcho(message: Message) {
//        val chatService = RestClient.getRetrofit().create(ChatRetrofitInterfaces::class.java)
//        chatService.sendRestEcho("$chatRoomId", ChatMessage("test", message.text)).enqueue(object :
//            Callback<ChatRetrofitResponse> {
//            override fun onResponse(call: Call<ChatRetrofitResponse>, response: Response<ChatRetrofitResponse>) {
//                if (response.isSuccessful) {
//                    Log.d("CHAT/SUCCESS", response.toString())
//                    val resp: ChatRetrofitResponse? = response.body()
//                    if (resp != null) {
//                        if (resp.isSuccess) {
//                            Log.d("CHAT/SUCCESS", "채팅 전송 성공")
//                        } else {
//                            Log.d("CHAT/SUCCESS", "채팅 전송 실패")
//                        }
//                    } else {
//                        Log.d("CHAT/SUCCESS", "Response body is null")
//                    }
//                } else {
//                    val errorBody = response.errorBody()?.string()
//                    if (errorBody != null) {
//                        Log.e("CHAT/SUCCESS", "Error sending rest echo: $errorBody")
//                        toast("Error sending rest echo: $errorBody")
//                    } else {
//                        Log.e("CHAT/SUCCESS", "Error sending rest echo: unknown error")
//                        toast("Error sending rest echo: unknown error")
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ChatRetrofitResponse>, t: Throwable) {
//                Log.e("CHAT/FAILURE", t.message.toString())
//                toast(t.message ?: "Unknown error")
//            }
//        })
//    }

    private fun subscribeToTopic() {
        val dispPing = mStompClient.topic("/sub/chat.message")
            .take(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message ->
                Log.d(TAG, "Received ${message.payload}")
                val echoModel = mGson.fromJson(message.payload, EchoModel::class.java)
                if (echoModel.content != null) {
                    addItem(echoModel)
                }
            }, { throwable ->
                Log.e(TAG, "Error on subscribe topic", throwable)
            })

        compositeDisposable.add(dispPing)
    }

    private fun addItem(echoModel: EchoModel) {
        if (echoModel.sender.equals("${BEARER_TOKEN}")) {
            return
        }
        val message = Message(echoModel.content ?: "", echoModel.sender ?: "Unknown", false, mTimeFormat.format(Date()))
        chatRVAdapter.addMessage(message)
        chatRVAdapter.notifyDataSetChanged()
        binding.recyclerView.scrollToPosition(chatRVAdapter.itemCount - 1)
    }

    private fun setupChatScreen() {
        chatRVAdapter = ChatRVAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = chatRVAdapter
    }

    private fun toast(text: String) {
        Log.i(TAG, text)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun applySchedulers(): CompletableTransformer {
        return CompletableTransformer { upstream ->
            upstream.unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun resetSubscriptions() {
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        disconnectStomp()
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}

data class EchoModel(
    var sender: String? = null,
    var content: String? = null
)
