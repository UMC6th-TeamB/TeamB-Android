package com.smumc.smumc_6th_teamc_android.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smumc.smumc_6th_teamc_android.databinding.ChatRoomItemBinding

// RecyclerView 어댑터 클래스 정의
class ChatRoomRVAdapter(private val chatRooms: ArrayList<ChatRoom>, private val onClick: (ChatRoom) -> Unit)
    : RecyclerView.Adapter<ChatRoomViewHolder>() {

    // ViewHolder를 생성할 때 호출되는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val binding = ChatRoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatRoomViewHolder(binding) // ChatRoomViewHolder 생성 및 반환
    }

    // ViewHolder에 데이터를 바인딩할 때 호출되는 메서드
    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(chatRooms[position], onClick) // 지정된 위치의 채팅방 데이터를 ViewHolder에 바인딩
    }

    // 아이템의 총 개수를 반환하는 메서드
    override fun getItemCount(): Int = chatRooms.size // 채팅방 목록의 크기 반환

    // 채팅방 목록을 업데이트하는 메서드
//    fun updateChatRooms(newChatRooms: ArrayList<ChatRoom>) {
//        chatRooms = newChatRooms // 새로운 채팅방 목록으로 업데이트
//        notifyDataSetChanged() // 데이터 변경을 어댑터에 알림
//    }
}

// ViewHolder 클래스 정의: 채팅방 아이템 뷰를 관리
class ChatRoomViewHolder(private val binding: ChatRoomItemBinding) : RecyclerView.ViewHolder(binding.root) {

    // 채팅방 데이터를 View에 바인딩하는 메서드
    fun bind(chatRoom: ChatRoom, onClick: (ChatRoom) -> Unit) {
        var time = ""
        if(chatRoom.dateTime.substring(11, 13).toInt() > 12){
            time = "오후 ${chatRoom.dateTime.substring(11, 13).toInt()-12}${chatRoom.dateTime.substring(13, 16)}"
        } else {
            time = "오전 ${chatRoom.dateTime.substring(11, 16)}"
        }
        binding.chatRoomDate.text = chatRoom.dateTime.substring(0, 10) // 채팅방 날짜 설정
        binding.chatRoomRegion.text = chatRoom.region // 채팅방 지역 설정
        binding.chatRoomMember.text = chatRoom.memberCount.toString() // 채팅방 멤버 수 설정
        binding.chatRoomMessage.text = chatRoom.lastMessage // 채팅방 메시지 설정
        binding.chatRoomTime.text = time
        binding.root.setOnClickListener { onClick(chatRoom) } // 아이템 클릭 리스너 설정
    }
}
