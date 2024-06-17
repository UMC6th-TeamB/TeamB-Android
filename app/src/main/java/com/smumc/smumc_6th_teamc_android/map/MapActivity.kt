package com.smumc.smumc_6th_teamc_android.map

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.SupportMapFragment
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMapBinding
import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationListener
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.smumc.smumc_6th_teamc_android.mypage.MypageActivity

class MapActivity : AppCompatActivity() {

    lateinit var binding: ActivityMapBinding
    private var stationDatas = ArrayList<Station>()
    private var numPeopleDatas = ArrayList<People>()
    private var timePicker: TimePicker? = null
    private var timeInterval = 5 // timePicker에서 설정할 분 간격 (=5분 간격)
    // 지도 권한 목록
    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    var myLocationListener: LocationListener? = null // 위치 측정 리스너
    lateinit var mainGoogleMap: GoogleMap // 구글 지도 객체를 담을 변수
    var myMarker: Marker? = null // 현재 사용자 위치에 표시되는 마커

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 구글 지도 셋팅
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener() //ClickListener 모음
        initTimePicker() //TimePicker 초기화
        requestPermissions(permissionList,0) //지도 및 위치 권한 허용 request

        // 마이페이지 버튼 클릭
        binding.mapMypageBtn.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }

        // 카풀하기 버튼 클릭 리스너 설정
        binding.mapCarpoolBtn.setOnClickListener {
            setLocationStatus(true)
        }

        // Google MapFragment 객체
        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.map_main) as SupportMapFragment

        // Google Map 사용 준비 완료 시 반응하는 리스너
        supportMapFragment.getMapAsync {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager // 위치 정보 관리하는 객체

            // 구글맵 객체 변수에 담아서 사용
            mainGoogleMap = it

            it.uiSettings.isZoomControlsEnabled = true // 지도 확대 축소 기능
            it.isMyLocationEnabled = true // 현재 위치 표시 기능
            it.uiSettings.isMyLocationButtonEnabled = false // Google에서 제공하는 현재 위치 표시 버튼 안 보이게

            it.setOnMapClickListener { // 지도 영역 클릭 시 선택 팝업창 닫히도록
                setLocationStatus(false)
                setTimeStatus(false)
                setPeopleStatus(false)
                setMatchingStatus(false)
            }

            if(checkLocationPermission()){ //권한 허용 여부 확인
                // 현재 저장되어 있는 위치 정보값
                val location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                if(location1 != null){
                    setMyLocation(location1)
                } else if(location2 != null){
                    setMyLocation(location2)
                }

                // 현재 위치 측정하여 지도 갱신
                getMyLocation()
            }
        }

        // 장소 데이터 리스트 생성 더미 데이터
        stationDatas.apply{
            add(Station("남영역 1번출구"))
            add(Station("홍제역 1번출구"))
            add(Station("서울역 버스환승센터 택시정류장"))
            add(Station("길음역 3번출구"))
            add(Station("광화문역 2번출구"))
            add(Station("경복궁역 3번출구"))
            add(Station("시청역 4번출구"))
        }

        // 인원수 데이터 리스트 생성 더미 데이터
        numPeopleDatas.apply{
            add(People("4명"))
            add(People("3명"))
            add(People("2명"))
            add(People("상관없음"))
        }

        // Station Adapter와 Datalist 연결
        val stationRVAdapter = StationRVAdapter(stationDatas)
        binding.mapLocationRv.adapter = stationRVAdapter
        binding.mapLocationRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        stationRVAdapter.setMyItemClickListener(object: StationRVAdapter.MyItemClickListener{
            //각 아이템 클릭 시 반응하는 함수
            override fun onItemClick(position: Int){
                stationRVAdapter.clickItem(position)
            }
            //아이템의 체크이미지 클릭 시 반응하는 함수
            override fun onCheckIconClick() {
                setLocationStatus(false)
                setTimeStatus(true)
            }
        })

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

    }
    private fun initClickListener(){
        // 카풀하기 버튼 클릭 리스너 설정
        binding.mapCarpoolBtn.setOnClickListener {
            setLocationStatus(true)
        }

        // 시간 설정 선택 버튼 클릭 시
        binding.mapSettingTimeBtn.setOnClickListener {
            setTimeStatus(false)
            setPeopleStatus(true)
        }

        // 현재 인원 매칭 요청 버튼 클릭 시 다이얼로그 표시하도록
        binding.mapCarpoolCurrentMatchingBtn.setOnClickListener {
            showCurrentMatchDialog()
        }

        // gps 버튼 클릭
        binding.mapCurrentLocationBtn.setOnClickListener {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager // 위치 정보 관리하는 객체

            if(checkLocationPermission()) { //권한 허용 여부 확인
                // 사용자 위치 정보 true로 변환
                mainGoogleMap.isMyLocationEnabled = true

                // 현재 저장되어 있는 위치 정보값 가져오기
                // 위치정보얻기: LocationManager의 getLastKnownLocation 이용
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null) {
                    setMyLocation(location)
                }

                // 현재 위치 측정하여 지도 갱신하는 함수
                getMyLocation()
            }

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

    // Time Picker 초기화
    private fun initTimePicker(){
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

    // Location 권한 확인 함수
    private fun checkLocationPermission(): Boolean {
        // 권한 확인(두 개의 권한에 대한 허용 여부 가져오기)
        // 거부: PERMISSION_DENIED
        // 허용: PERMISSION_GRANTED
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // 매개변수의 위도 경도값(Location)에 해당하는 위치로 이동시키는 함수
    fun setMyLocation(location: Location){
        // 위치 측정을 중단
        if(myLocationListener != null) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.removeUpdates(myLocationListener!!)
            myLocationListener = null
        }

        // 위도와 경도를 관리하는 객체
        val latLng = LatLng(location.latitude, location.longitude)

        // 지도를 이용시키기 위한 객체를 생성
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)

        // 지도를 이동
        mainGoogleMap.animateCamera(cameraUpdate)

        // 현재 위치에 마커를 표시
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)

        // 마커 이미지 변경
        val markerBitmap = BitmapDescriptorFactory.fromResource(R.drawable.googlemap_marker)
        markerOptions.icon(markerBitmap)

        // 기존에 표시한 마커를 제거한다.
        if(myMarker != null){
            myMarker?.remove()
            myMarker = null
        }
        mainGoogleMap.addMarker(markerOptions)
    }

    // 현재 위치 값 가져오는 함수
    fun getMyLocation(){
        // 두 권한에 대하여 허용 되어 있으면
        if(checkLocationPermission()){

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            // 측정을 중단시키려면 위치 측정 리스너 객체 필요
            // 위치 측정 리스너
            myLocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    setMyLocation(location) //현재 위치로 이동
                }
            }

            // 위치 측정 요청
            // GPS 활성화 되어 있으면(GPS_PROVIDER == true) 위치 업데이트 요청
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0f, myLocationListener!!)
            }
        }
    }
}

