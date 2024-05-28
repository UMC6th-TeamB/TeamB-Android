package com.smumc.smumc_6th_teamc_android.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMapBinding
import com.smumc.smumc_6th_teamc_android.databinding.ItemLocationBinding
import java.text.FieldPosition

class MapActivity : AppCompatActivity() {

    lateinit var binding: ActivityMapBinding
    private var stationDatas = ArrayList<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 카풀하기 버튼 클릭 리스너 설정
        binding.mapCarpoolBtn.setOnClickListener {
            setLocationStatus(true)
        }

        binding.mapMain.setOnTouchListener { _, event ->
            setLocationStatus(false)
            true
        }

        // 데이터 리스트 생성 더미 데이터
        stationDatas.apply{
            add(Location("남영역 1번출구"))
            add(Location("홍제역 1번출구"))
            add(Location("서울역 버스환승센터 택시정류장"))
            add(Location("길음역 3번출구"))
            add(Location("광화문역 2번출구"))
            add(Location("경복궁역 3번출구"))
            add(Location("시청역 4번출구"))
        }

        // Adapter와 Datalist 연결
        val locationRVAdapter = LocationRVAdapter(stationDatas)
        binding.mapLocationRv.adapter = locationRVAdapter
        binding.mapLocationRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        locationRVAdapter.setMyItemClickListener(object: LocationRVAdapter.MyItemClickListener{
            override fun onItemClick(position: Int){
                locationRVAdapter.clickItem(position)
            }
        })

    }

    private fun setLocationStatus(carpool: Boolean){
        if(carpool){
            binding.mapLocationRv.visibility = View.VISIBLE
            binding.mapLocationPannelLine.visibility = View.VISIBLE
        }
        else{
            binding.mapLocationRv.visibility = View.GONE
            binding.mapLocationPannelLine.visibility = View.GONE
        }
    }
}

