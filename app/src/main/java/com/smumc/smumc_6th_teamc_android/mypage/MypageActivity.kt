package com.smumc.smumc_6th_teamc_android.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMypageBinding
import com.smumc.smumc_6th_teamc_android.login.LoginActivity
import com.smumc.smumc_6th_teamc_android.login.api.Nickname
import com.smumc.smumc_6th_teamc_android.login.api.UserRetrofitItf
import com.smumc.smumc_6th_teamc_android.login.api.UserRetrofitObj
import com.smumc.smumc_6th_teamc_android.login.api.UserRetrofitResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageActivity : AppCompatActivity() {

    lateinit var binding: ActivityMypageBinding
    private var nickNameChange: String = ""
    private var BEARER_TOKEN: String? = "" // 로그인 토큰 값 전달 받는 변수 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //MapActivity에서 토큰 값 전달 받음
        BEARER_TOKEN = intent.getStringExtra("BearerToken")

        // Bearer 제거
        BEARER_TOKEN = BEARER_TOKEN?.substring(8).toString()
        Log.d("MYPAGE 토큰 값", BEARER_TOKEN.toString())

        // 백버튼 클릭 시
        binding.backIb.setOnClickListener {
            finish() // 현재 액티비티 종료
        }

        // 로그아웃 버튼 클릭 시
        binding.logoutIb.setOnClickListener {

            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        // 마이페이지 조회 API 연결
        val authService = UserRetrofitObj.getRetrofit().create(UserRetrofitItf::class.java)
        authService.myPage("Bearer $BEARER_TOKEN").enqueue(object:
            Callback<UserRetrofitResponse> {
            override fun onResponse(call: Call<UserRetrofitResponse>, response: Response<UserRetrofitResponse>){
                Log.d("MYPAGE/SUCCESS", response.toString())
                val resp: UserRetrofitResponse = response.body()!!
                if (resp != null){
                    if (resp.isSuccess){
                        val result = resp.result // 토큰 결과 값 저장 (->닉네임 저장)
                        binding.profileNameTv.text = result // 닉네임 적용
                        //BEARER_TOKEN = result // 토큰 값 저장
                        Log.d("MYPAGE/SUCCESS", "토큰 저장 성공: $BEARER_TOKEN")
                    } else {
                        Log.d("MYPAGE/SUCCESS", "마이페이지 조회 실패")
                    }
                } else {
                    Log.d("MYPAGE/SUCCESS", "Response body is null")
                }
            }

            override fun onFailure(call: Call<UserRetrofitResponse>, t: Throwable) {
                Log.d("MYPAGE/FAILURE", t.message.toString())
            }

        })


        // 변경 버튼 클릭 시
        binding.nameChangeBt.setOnClickListener {
            // 닉네임 수정 저장
            nickNameChange = binding.nameChangeEv.text.toString()
            Log.d("닉네임 변경", nickNameChange)

            // 닉네임 수정 API 연결
            val nickNameService = UserRetrofitObj.getRetrofit().create(UserRetrofitItf::class.java)
            nickNameService.changeNickname("Bearer $BEARER_TOKEN", Nickname(nickNameChange)).enqueue(object:
                Callback<UserRetrofitResponse> {
                override fun onResponse(call: Call<UserRetrofitResponse>, response: Response<UserRetrofitResponse>){
                    Log.d("CHANGENICKNAME/SUCCESS", response.toString())
                    val resp: UserRetrofitResponse = response.body()!!
                    if (resp != null){
                        if (resp.isSuccess){
                            binding.profileNameTv.text = nickNameChange // 닉네임 적용
                            Log.d("CHANGE/SUCCESS", "토큰 저장 성공: $BEARER_TOKEN")
                        } else {
                            Log.d("CHANGE/SUCCESS", "닉네임 수정 실패")
                        }
                    } else {
                        Log.d("CHANGE/SUCCESS", "Response body is null")
                    }
                }

                override fun onFailure(call: Call<UserRetrofitResponse>, t: Throwable) {
                    Log.d("CHANGE/FAILURE", t.message.toString())
                }

            })


        }



    }
}