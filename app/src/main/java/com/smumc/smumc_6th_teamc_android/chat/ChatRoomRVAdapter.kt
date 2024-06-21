// src/main/java/com/smumc/smumc_6th_teamc_android/chat/ChatRoomAdapter.kt
package com.smumc.smumc_6th_teamc_android.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smumc.smumc_6th_teamc_android.databinding.ChatRoomItemBinding

data class ChatRoomRVAdapter(val date: String, val region: String, val message: String, val time: String, val members: List<Member>)

class ChatRoomAdapter(private val chatRooms: List<ChatRoom>, private val onClick: (ChatRoom) -> Unit)
    : RecyclerView.Adapter<ChatRoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val binding = ChatRoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(chatRooms[position], onClick)
    }

    override fun getItemCount(): Int = chatRooms.size
}

class ChatRoomViewHolder(private val binding: ChatRoomItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chatRoom: ChatRoom, onClick: (ChatRoom) -> Unit) {
        binding.chatRoomDate.text = chatRoom.date
        binding.chatRoomRegion.text = chatRoom.region
        binding.chatRoomMember.text = chatRoom.members.size.toString()
        binding.chatRoomMessage.text = chatRoom.message
        binding.chatRoomTime.text = chatRoom.time
        binding.root.setOnClickListener { onClick(chatRoom) }
    }
}
