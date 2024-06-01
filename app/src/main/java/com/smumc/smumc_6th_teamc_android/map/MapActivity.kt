package com.smumc.smumc_6th_teamc_android.map

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMapBinding


class MapActivity : AppCompatActivity() {

    lateinit var binding: ActivityMapBinding
    private var stationDatas = ArrayList<Location>()
    private var numPeopleDatas = ArrayList<People>()
    private var timePicker: TimePicker? = null
    private var timeInterval = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var minute = timePicker?.minute // TimePicker에서 minute 부분을 가져옴

        //TimePicker 연결 및 초기화 (오전/오후 없앰)
        timePicker = binding.mapSettingTimePickerTp
        timePicker?.setIs24HourView(true)

        // TimePicker의 분을 5분 간격으로 설정
        setTimePickerInterval(timePicker)

        // TimePicker에 5분 간격으로 표시
        if(minute != null){
            if (minute % timeInterval !== 0) {
                val minuteFloor: Int = minute + timeInterval - minute % timeInterval
                minute = minuteFloor + if (minute === minuteFloor + 1) timeInterval else 0
                if (minute >= 60) {
                    minute = minute % 60
                }
                timePicker!!.currentMinute = minute / timeInterval
            }
        }

        // 카풀하기 버튼 클릭 리스너 설정
        binding.mapCarpoolBtn.setOnClickListener {
            setLocationStatus(true)
        }

        // 지도 영역 터치 시 띄어진 팝업창들 사라지도록
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
            //각 아이템 클릭 시 반응하는 함수
            override fun onItemClick(position: Int){
                locationRVAdapter.clickItem(position)
            }
            //아이템의 체크이미지 클릭 시 반응하는 함수
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
            //각 아이템 클릭 시 반응하는 함수
            override fun onItemClick(position: Int){
                peopleRVAdapter.clickItem(position)
            }
            //아이템의 체크이미지 클릭 시 반응하는 함수
            override fun onCheckIconClick() {
                setPeopleStatus(false)
                setMatchingStatus(true)
            }
        })

        // 현재 인원 매칭 요청 버튼 클릭 시 다이얼로그 표시하도록
        binding.mapCarpoolCurrentMatchingBtn.setOnClickListener {
            showCurrentMatchDialog()
        }

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

    // 커스텀 다이얼로그를 표시하는 함수
    private fun showCurrentMatchDialog() {
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 기존 다이어그램 배경 투명으로 적용(커스텀한 배경이 보이게 하기 위함)
        dialog.setContentView(R.layout.activity_map_current_match)

        // 다이얼로그 레이아웃에서 YES 및 NO 버튼을 찾고 변수에 할당
        val yesButton: Button = dialog.findViewById(R.id.current_match_yes_btn)
        val noButton: Button = dialog.findViewById(R.id.current_match_no_btn)

        yesButton.setOnClickListener {
            // YES 버튼 클릭 시 수행할 작업
            dialog.dismiss() // 다이얼로그 닫기
            binding.mapCarpoolMatchingLo.visibility = View.GONE
        }

        noButton.setOnClickListener {
            // NO 버튼 클릭 시 수행할 작업
            dialog.dismiss() // 다이얼로그 닫기
        }

        // 다이얼로그 표시
        dialog.show()
    }

    // Time Picker를 5분 간격으로 설정하는 함수
    private fun setTimePickerInterval(timePicker: TimePicker?) {
        try {
            val minutePicker = timePicker!!.findViewById<View>(
                Resources.getSystem().getIdentifier(
                    "minute", "id", "android"
                )
            ) as NumberPicker
            minutePicker.setMinValue(0)
            minutePicker.setMaxValue(60 / timeInterval - 1)
            val displayedValues: MutableList<String> = ArrayList()
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += timeInterval
            }
            minutePicker.setDisplayedValues(displayedValues.toTypedArray<String>())
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Exception: $e")
        }
    }
}

