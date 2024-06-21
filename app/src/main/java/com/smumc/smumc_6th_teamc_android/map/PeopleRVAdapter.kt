package com.smumc.smumc_6th_teamc_android.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ItemPeopleBinding

class PeopleRVAdapter (private val numList: ArrayList<People>): RecyclerView.Adapter<PeopleRVAdapter.ViewHolder>() {

    interface PeopleItemClickListener {
        fun onItemClick(position: Int, people: People) // 각 인원수 아이템 클릭 시 반응하는 함수
        fun onCheckIconClick(people: People) //체크 이미지 클릭 시 반응하는 함수
    }

    // 외부에서 전달받은 Listener 객체를 Adapter에서 사용할 수 있도록 따로 저장할 변수 선언
    private lateinit var mItemClickListener: PeopleItemClickListener
    fun setMyItemClickListener(itemClickListener: PeopleItemClickListener) {
        mItemClickListener = itemClickListener
    }

    // 현재 선택된 아이템의 인덱스를 저장할 변수
    private var selectedLocation = RecyclerView.NO_POSITION

    // 현재 클릭한 인원수의 인덱스를 selectedLocation 변수에 저장하고, 이전의 클릭된 인덱스를 previousPosition에 저장
    fun clickItem(position: Int) {
        val previousPosition = selectedLocation
        selectedLocation = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedLocation)
    }

    // 선택했던 item 초기화하는 메서드
    fun clearSelection() {
        val previousPosition = selectedLocation
        selectedLocation = RecyclerView.NO_POSITION
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

        // 인원수 아이템 선택에 따른 반응
        holder.itemView.isSelected = (selectedLocation == position)
        holder.binding.itemMapSelectPeople.setOnClickListener {
            mItemClickListener.onItemClick(position, numList[position])
            clickItem(position)
        }
        holder.select(holder.itemView.isSelected) //선택된 item 표시 효과

        // 선택한 인원수의 check icon 클릭했을 때의 반응
        holder.binding.itemMapSelectPeopleIv.setOnClickListener {
            mItemClickListener.onCheckIconClick(numList[position])
        }
    }

    override fun getItemCount(): Int = numList.size

    inner class ViewHolder(val binding: ItemPeopleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(people: People) {
            binding.itemMapSelectPeopleTv.text = people.numPeople
        }

        fun select(isSelect: Boolean) {
            if (isSelect) { // 선택했을 때의 반응
                binding.itemMapSelectPeople.setBackgroundColor(itemView.context.getColor(R.color.smupool_white))
                binding.itemMapSelectPeopleTv.setTextColor(itemView.context.getColor(R.color.smupool_blue))
                binding.itemMapSelectPeopleIv.visibility = View.VISIBLE
            } else { // 선택하지 않았을 때의 반응
                binding.itemMapSelectPeople.setBackgroundColor(itemView.context.getColor(android.R.color.transparent))
                binding.itemMapSelectPeopleTv.setTextColor(itemView.context.getColor(android.R.color.black))
                binding.itemMapSelectPeopleIv.visibility = View.GONE
            }
        }
    }
}