package com.smumc.smumc_6th_teamc_android.chat

import android.view.LayoutInflater // 레이아웃을 인플레이트하기 위해 필요
import android.view.View // View 클래스를 사용하기 위해 필요
import android.view.ViewGroup // ViewGroup 클래스를 사용하기 위해 필요
import android.widget.ImageView // ImageView 클래스를 사용하기 위해 필요
import android.widget.TextView // TextView 클래스를 사용하기 위해 필요
import androidx.recyclerview.widget.RecyclerView // RecyclerView 클래스를 사용하기 위해 필요
import com.smumc.smumc_6th_teamc_android.R // 리소스 파일 접근을 위해 필요

// Message 데이터 클래스 정의
data class Message(val text: String, val userInfo: String, val isSentByUser: Boolean) // 메시지 텍스트, 사용자 정보, 사용자가 보낸 메시지 여부

// ChatRVAdapter 클래스 정의
class ChatRVAdapter(private val messages: MutableList<Message>) : // RecyclerView 어댑터 상속
    RecyclerView.Adapter<RecyclerView.ViewHolder>() { // RecyclerView.ViewHolder를 사용하는 어댑터

    companion object { // 컴패니언 오브젝트 정의
        private const val VIEW_TYPE_OTHER = 1 // 다른 사용자의 메시지 타입
        private const val VIEW_TYPE_MINE = 2 // 내 메시지 타입
    }

    // 다른 사용자의 메시지를 위한 ViewHolder 클래스
    inner class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userAvatar: ImageView = itemView.findViewById(R.id.user_avatar) // 사용자 아바타 이미지뷰
        val messageText: TextView = itemView.findViewById(R.id.message_text) // 메시지 텍스트뷰
    }

    // 내 메시지를 위한 ViewHolder 클래스
    inner class MineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.message_text) // 메시지 텍스트뷰
    }

    // 메시지 타입에 따라 뷰 타입 반환
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSentByUser) VIEW_TYPE_MINE else VIEW_TYPE_OTHER // 사용자가 보낸 메시지인지 여부에 따라 뷰 타입 결정
    }

    // ViewHolder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MINE) { // 내 메시지 타입인 경우
            val view = LayoutInflater.from(parent.context).inflate(R.layout.message_mine, parent, false) // 내 메시지 레이아웃 인플레이트
            MineViewHolder(view) // MineViewHolder 생성
        } else { // 다른 사용자의 메시지 타입인 경우
            val view = LayoutInflater.from(parent.context).inflate(R.layout.message_other, parent, false) // 다른 사용자의 메시지 레이아웃 인플레이트
            OtherViewHolder(view) // OtherViewHolder 생성
        }
    }

    // ViewHolder에 데이터를 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position] // 현재 위치의 메시지 가져오기
        if (holder is MineViewHolder) { // 내 메시지 ViewHolder인 경우
            holder.messageText.text = message.text // 메시지 텍스트 설정
        } else if (holder is OtherViewHolder) { // 다른 사용자의 메시지 ViewHolder인 경우
            holder.messageText.text = message.text // 메시지 텍스트 설정
        }
    }

    // 아이템의 총 개수를 반환
    override fun getItemCount(): Int {
        return messages.size // 메시지 리스트의 크기 반환
    }

    // 새로운 메시지를 추가하는 메서드
    fun addMessage(message: Message) {
        messages.add(message) // 메시지 리스트에 추가
        notifyItemInserted(messages.size - 1) // 새 아이템이 삽입되었음을 어댑터에 알림
    }
}
