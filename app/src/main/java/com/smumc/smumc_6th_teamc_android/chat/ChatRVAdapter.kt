package com.smumc.smumc_6th_teamc_android.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smumc.smumc_6th_teamc_android.R

// 메시지 데이터를 나타내는 데이터 클래스
data class Message(val text: String, val userInfo: String, val isSentByUser: Boolean, val time: String)

// RecyclerView 어댑터 클래스 정의
class ChatRVAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 뷰 타입 상수를 정의하는 컴패니언 오브젝트
    companion object {
        private const val VIEW_TYPE_OTHER = 1 // 다른 사용자의 메시지 뷰 타입
        private const val VIEW_TYPE_MINE = 2 // 자신의 메시지 뷰 타입
    }

    // 다른 사용자의 메시지를 표시하는 ViewHolder 클래스
    inner class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userAvatar: ImageView = itemView.findViewById(R.id.user_avatar) // 사용자 아바타 이미지뷰
        val messageText: TextView = itemView.findViewById(R.id.message_text) // 메시지 텍스트뷰
        val messageTime: TextView = itemView.findViewById(R.id.message_time) // 메시지 시간 텍스트뷰
    }

    // 자신의 메시지를 표시하는 ViewHolder 클래스
    inner class MineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.message_text) // 메시지 텍스트뷰
        val messageTime: TextView = itemView.findViewById(R.id.message_time) // 메시지 시간 텍스트뷰
    }

    // 메시지의 뷰 타입을 반환하는 메서드
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSentByUser) VIEW_TYPE_MINE else VIEW_TYPE_OTHER // 사용자가 보낸 메시지인지 여부에 따라 뷰 타입 결정
    }

    // ViewHolder를 생성하는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MINE) { // 자신의 메시지 뷰 타입인 경우
            val view = LayoutInflater.from(parent.context).inflate(R.layout.message_mine, parent, false) // 자신의 메시지 레이아웃 인플레이트
            MineViewHolder(view) // MineViewHolder 생성 및 반환
        } else { // 다른 사용자의 메시지 뷰 타입인 경우
            val view = LayoutInflater.from(parent.context).inflate(R.layout.message_other, parent, false) // 다른 사용자의 메시지 레이아웃 인플레이트
            OtherViewHolder(view) // OtherViewHolder 생성 및 반환
        }
    }

    // ViewHolder에 데이터를 바인딩하는 메서드
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position] // 현재 위치의 메시지 가져오기
        if (holder is MineViewHolder) { // 자신의 메시지 ViewHolder인 경우
            holder.messageText.text = message.text // 메시지 텍스트 설정
            holder.messageTime.text = message.time // 메시지 시간 설정
        } else if (holder is OtherViewHolder) { // 다른 사용자의 메시지 ViewHolder인 경우
            holder.messageText.text = message.text // 메시지 텍스트 설정
            holder.messageTime.text = message.time // 메시지 시간 설정
        }
    }

    // 아이템의 총 개수를 반환하는 메서드
    override fun getItemCount(): Int {
        return messages.size // 메시지 리스트의 크기 반환
    }

    // 새로운 메시지를 추가하는 메서드
    fun addMessage(message: Message) {
        messages.add(message) // 메시지 리스트에 추가
        notifyItemInserted(messages.size - 1) // 새 아이템이 삽입되었음을 어댑터에 알림
    }

    // 메시지 리스트를 업데이트하는 메서드
    fun updateMessages(newMessages: List<Message>) {
        messages.clear() // 기존 메시지 리스트 초기화
        messages.addAll(newMessages) // 새로운 메시지 리스트 추가
        notifyDataSetChanged() // 데이터 세트 변경을 어댑터에 알림
    }
}
