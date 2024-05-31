package com.smumc.smumc_6th_teamc_android.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ItemPeopleBinding

class PeopleRVAdapter (private val numList: ArrayList<People>): RecyclerView.Adapter<PeopleRVAdapter.ViewHolder>() {

    interface PeopleItemClickListener {
        fun onItemClick(position: Int)
        fun onCheckIconClick()
    }

    // 외부에서 전달받은 Listener 객체를 Adapter에서 사용할 수 있도록 따로 저장할 변수 선언
    private lateinit var mItemClickListener: PeopleItemClickListener
    fun setMyItemClickListener(itemClickListener: PeopleItemClickListener) {
        mItemClickListener = itemClickListener
    }

    // 현재 선택된 아이템의 인덱스를 저장할 변수
    private var selectedLocation = RecyclerView.NO_POSITION

    fun clickItem(position: Int) {
        val previousPosition = selectedLocation
        selectedLocation = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedLocation)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PeopleRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemPeopleBinding = ItemPeopleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeopleRVAdapter.ViewHolder, position: Int) {
        holder.bind(numList[position])

        // 인원 선택
        holder.itemView.isSelected = (selectedLocation == position)
        holder.binding.itemMapSelectPeople.setOnClickListener {
            mItemClickListener.onItemClick(position)
            clickItem(position)
        }
        holder.select(holder.itemView.isSelected)

        // 선택한 장소의 check icon 클릭
        holder.binding.itemMapSelectPeopleIv.setOnClickListener {
            mItemClickListener.onCheckIconClick()
        }
    }

    override fun getItemCount(): Int = numList.size

    inner class ViewHolder(val binding: ItemPeopleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(people: People) {
            binding.itemMapSelectPeopleTv.text = people.numPeople
        }

        fun select(isSelect: Boolean) {
            if (isSelect) {
                binding.itemMapSelectPeople.setBackgroundColor(itemView.context.getColor(R.color.smupool_white))
                binding.itemMapSelectPeopleTv.setTextColor(itemView.context.getColor(R.color.smupool_blue))
                binding.itemMapSelectPeopleIv.visibility = View.VISIBLE
            } else {
                binding.itemMapSelectPeople.setBackgroundColor(itemView.context.getColor(android.R.color.transparent))
                binding.itemMapSelectPeopleTv.setTextColor(itemView.context.getColor(android.R.color.black))
                binding.itemMapSelectPeopleIv.visibility = View.GONE
            }
        }
    }
}