package com.smumc.smumc_6th_teamc_android.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smumc.smumc_6th_teamc_android.databinding.ItemPersonalBinding

class PersonalRVAdapter(private val personalList: ArrayList<Personal>): RecyclerView.Adapter<PersonalRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGrop: ViewGroup, viewType: Int): PersonalRVAdapter.ViewHolder {
        val binding: ItemPersonalBinding = ItemPersonalBinding.inflate(LayoutInflater.from(viewGrop.context), viewGrop, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonalRVAdapter.ViewHolder, position: Int) {
        holder.bind(personalList[position])
    }

    override fun getItemCount(): Int = personalList.size

    inner class ViewHolder(val binding: ItemPersonalBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(personal: Personal){
            binding.title.text = personal.title
            binding.content.text = personal.content
        }
    }
}