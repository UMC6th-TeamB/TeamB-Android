package com.smumc.smumc_6th_teamc_android.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ItemLocationBinding

class StationRVAdapter(private val stationList: ArrayList<Station>): RecyclerView.Adapter<StationRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(position: Int) // 각 장소 아이템 클릭 시 반응하는 함수
        fun onCheckIconClick() //체크 이미지 클릭 시 반응하는 함수
    }

    // 외부에서 전달받은 Listener 객체를 Adapter에서 사용할 수 있도록 따로 저장할 변수 선언
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    // 현재 선택된 아이템의 인덱스를 저장할 변수
    private var selectedLocation = RecyclerView.NO_POSITION

    // 현재 클릭한 장소의 인덱스를 selectedLocation 변수에 저장하고, 이전의 클릭된 인덱스를 previousPosition에 저장
    fun clickItem(position: Int) {
        val previousPosition = selectedLocation
        selectedLocation = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedLocation)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StationRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemLocationBinding = ItemLocationBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationRVAdapter.ViewHolder, position: Int) {
        holder.bind(stationList[position])

        // 장소 아이템 선택에 따른 반응
        holder.itemView.isSelected = (selectedLocation == position)
        holder.binding.itemMapLocation.setOnClickListener {
            mItemClickListener.onItemClick(position)
            clickItem(position)
        }
        holder.select(holder.itemView.isSelected) //선택된 장소의 인덱스 부분을 select(true)로 함

        // 선택한 장소의 check icon 클릭했을 때의 반응
        holder.binding.itemMapSelectLocationIv.setOnClickListener {
            mItemClickListener.onCheckIconClick()
        }
    }

    override fun getItemCount(): Int = stationList.size

    inner class ViewHolder(val binding: ItemLocationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Station) {
            binding.itemMapLocationTv.text = location.station
        }

        fun select(isSelect: Boolean) {
            if (isSelect) { // 선택했을 때의 반응
                binding.itemMapLocation.setBackgroundColor(itemView.context.getColor(R.color.smupool_white))
                binding.itemMapLocationTv.setTextColor(itemView.context.getColor(R.color.smupool_blue))
                binding.itemMapSelectLocationIv.visibility = View.VISIBLE
            } else { // 선택하지 않았을 때의 반응
                binding.itemMapLocation.setBackgroundColor(itemView.context.getColor(android.R.color.transparent))
                binding.itemMapLocationTv.setTextColor(itemView.context.getColor(android.R.color.black))
                binding.itemMapSelectLocationIv.visibility = View.GONE
            }
        }
    }
}
