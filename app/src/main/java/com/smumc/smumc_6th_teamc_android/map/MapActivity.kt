package com.smumc.smumc_6th_teamc_android.map

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMapBinding
import com.smumc.smumc_6th_teamc_android.mypage.MypageActivity

class MapActivity : AppCompatActivity() {

    lateinit var binding: ActivityMapBinding
    private var stationDatas = ArrayList<Location>()
    private var numPeopleDatas = ArrayList<People>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 마이페이지 버튼 클릭
        binding.mapMypageBtn.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }

        // 카풀하기 버튼 클릭 리스너 설정
        binding.mapCarpoolBtn.setOnClickListener {
            setLocationStatus(true)
        }

        binding.mapMain.setOnTouchListener { _, event ->
            setLocationStatus(false)
            setTimeStatus(false)
            setPeopleStatus(false)
            setMatchingStatus(false)
            true
        }

        // 장소 데이터 리스트 생성 더미 데이터
        stationDatas.apply{
            add(Location("남영역 1번출구"))
            add(Location("홍제역 1번출구"))
            add(Location("서울역 버스환승센터 택시정류장"))
            add(Location("길음역 3번출구"))
            add(Location("광화문역 2번출구"))
            add(Location("경복궁역 3번출구"))
            add(Location("시청역 4번출구"))
        }

        // 인원수 데이터 리스트 생성 더미 데이터
        numPeopleDatas.apply{
            add(People("4명"))
            add(People("3명"))
            add(People("2명"))
            add(People("상관없음"))
        }

        // Location Adapter와 Datalist 연결
        val locationRVAdapter = LocationRVAdapter(stationDatas)
        binding.mapLocationRv.adapter = locationRVAdapter
        binding.mapLocationRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        locationRVAdapter.setMyItemClickListener(object: LocationRVAdapter.MyItemClickListener{
            override fun onItemClick(position: Int){
                locationRVAdapter.clickItem(position)
            }
            override fun onCheckIconClick() {
                setLocationStatus(false)
                setTimeStatus(true)
            }
        })

        // 시간 설정 선택 버튼 클릭 시
        binding.mapSettingTimeBtn.setOnClickListener {
            setTimeStatus(false)
            setPeopleStatus(true)
        }

        // People Adapter와 Datalist 연결
        val peopleRVAdapter = PeopleRVAdapter(numPeopleDatas)
        binding.mapSelectPeopleRv.adapter = peopleRVAdapter
        binding.mapSelectPeopleRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        peopleRVAdapter.setMyItemClickListener(object: PeopleRVAdapter.PeopleItemClickListener{
            override fun onItemClick(position: Int){
                peopleRVAdapter.clickItem(position)
            }
            override fun onCheckIconClick() {
                setPeopleStatus(false)
                setMatchingStatus(true)
            }
        })

    }

    // 장소 선택 레이아웃 보임 상태
    private fun setLocationStatus(carpool: Boolean){
        if(carpool){
            binding.mapLocationRv.visibility = View.VISIBLE
            binding.mapLocationPannelLineIv.visibility = View.VISIBLE
        }
        else{
            binding.mapLocationRv.visibility = View.GONE
            binding.mapLocationPannelLineIv.visibility = View.GONE
        }
    }

    // 시간 선택 레이아웃 보임 상태
    private fun setTimeStatus(select: Boolean){
        if(select){
            binding.mapSettingTimeLo.visibility = View.VISIBLE
        }
        else{
            binding.mapSettingTimeLo.visibility = View.GONE
        }
    }

    // 인원수 선택 레이아웃 보임 상태
    private fun setPeopleStatus(select: Boolean){
        if(select){
            binding.mapSelectPeopleRv.visibility = View.VISIBLE
            binding.mapPeoplePannelLineIv.visibility = View.VISIBLE
        }
        else{
            binding.mapSelectPeopleRv.visibility = View.GONE
            binding.mapPeoplePannelLineIv.visibility = View.GONE
        }
    }

    // 매칭 화면 레이아웃 보임 상태
    private fun setMatchingStatus(select: Boolean){
        if(select){
            binding.mapCarpoolMatchingLo.visibility = View.VISIBLE
        }
        else{
            binding.mapCarpoolMatchingLo.visibility = View.GONE
        }
    }
}

