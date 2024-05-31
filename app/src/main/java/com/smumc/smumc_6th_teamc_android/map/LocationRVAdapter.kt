package com.smumc.smumc_6th_teamc_android.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ItemLocationBinding

class LocationRVAdapter(private val stationList: ArrayList<Location>): RecyclerView.Adapter<LocationRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(position: Int)
        fun onCheckIconClick()
    }

    // 외부에서 전달받은 Listener 객체를 Adapter에서 사용할 수 있도록 따로 저장할 변수 선언
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LocationRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemLocationBinding = ItemLocationBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationRVAdapter.ViewHolder, position: Int) {
        holder.bind(stationList[position])

        // 장소 선택
        holder.itemView.isSelected = (selectedLocation == position)
        holder.binding.itemMapLocation.setOnClickListener {
            mItemClickListener.onItemClick(position)
            clickItem(position)
        }
        holder.select(holder.itemView.isSelected)

        // 선택한 장소의 check icon 클릭
        holder.binding.itemMapSelectLocationIv.setOnClickListener {
            mItemClickListener.onCheckIconClick()
        }
    }

    override fun getItemCount(): Int = stationList.size

    inner class ViewHolder(val binding: ItemLocationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Location) {
            binding.itemMapLocationTv.text = location.station
        }

        fun select(isSelect: Boolean) {
            if (isSelect) {
                binding.itemMapLocation.setBackgroundColor(itemView.context.getColor(R.color.smupool_white))
                binding.itemMapLocationTv.setTextColor(itemView.context.getColor(R.color.smupool_blue))
                binding.itemMapSelectLocationIv.visibility = View.VISIBLE
            } else {
                binding.itemMapLocation.setBackgroundColor(itemView.context.getColor(android.R.color.transparent))
                binding.itemMapLocationTv.setTextColor(itemView.context.getColor(android.R.color.black))
                binding.itemMapSelectLocationIv.visibility = View.GONE
            }
        }
    }
}
