package com.smumc.smumc_6th_teamc_android.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.smumc.smumc_6th_teamc_android.R
import androidx.core.content.ContextCompat
import com.smumc.smumc_6th_teamc_android.databinding.ActivitySignUpCheckBinding
import com.smumc.smumc_6th_teamc_android.login.api.CertificationNum
import com.smumc.smumc_6th_teamc_android.login.api.UserRetrofitItf
import com.smumc.smumc_6th_teamc_android.login.api.UserRetrofitObj
import com.smumc.smumc_6th_teamc_android.login.api.UserRetrofitResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class SignUpCheckActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignUpCheckBinding
    private var total = 180 // 3분 타이머
    private var started = true
    private var checkNum: String = ""
    private var studentId: String? = ""
    private var password: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SignUpActivity에서 학번과 비번 전달 받음
        studentId = intent.getStringExtra("studentId")
        password = intent.getStringExtra("password")

        // 전달 받은 학번으로 TextView 변경
        binding.userIdTv.text = studentId + "@sangmyung.kr로"

        // 3분 타이머 (바로 시작)
        val handler = Handler(mainLooper)
        thread(start = true){
            while(started && total !=0){
                Thread.sleep(1000) // 1초 지연

                total -= 1

                val minute = String.format("%02d", total / 60) // 분
                val second = String.format("%02d", total % 60) // 초
                handler.post{
                    binding.timerTv.text = "$minute:$second"
                    if (total < 60){ // 1분 남았을 때, 텍스트 색상 빨간색으로 변경
                        binding.timerTv.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
                    }
                }
            }

            // 03:00으로 타이머 초기화
            total = 180
            handler.post{
                binding.timerTv.text = "03:00"
                binding.timerTv.setTextColor(ContextCompat.getColor(this, R.color.smupool_navy))
                Toast.makeText(this, "인증 시간이 초과되었습니다. 회원가입을 다시 진행해 주세요.", Toast.LENGTH_SHORT).show()
                finish() // 액티비티 종료 -> 이전 액티비티인 SignUpActivity로 이동
            }
        }

        // 회원가입 버튼 클릭 시
        binding.signUpStartBt.setOnClickListener {

            // 인증 번호 저장
            checkNum = binding.signUpCheckNumberEt.text.toString()

            // 인증 번호 확인하는 함수 호출
            signCheckUp()
        }

        // 인증번호 EditText 색상 원상 복구
        binding.signUpCheckNumberEtEr.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                binding.signUpCheckNumberEt.visibility = View.VISIBLE
                binding.signUpCheckNumberEtEr.visibility = View.GONE
                binding.checkError.visibility = View.GONE
            }
        }

        // 배경화면 클릭 시 키보드 숨기기
        binding.signUpCheckActivity.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
            }
            true
        }
    }

    private fun signCheckUp() { // 인증번호 확인하는 함수

        // 인증번호를 입력하지 않은 경우 (빈칸)
        if (binding.signUpCheckNumberEt.text.toString().isEmpty()){

            // 인증번호 (visible or gone)
            binding.signUpCheckNumberEt.visibility = View.GONE
            binding.signUpCheckNumberEtEr.visibility = View.VISIBLE

            // 입력 오류 시 멘트 (visible)
            binding.checkError.visibility = View.VISIBLE

            // 회원가입이 실패했으므로 EditText를 비워줌 (사용자가 입력한 값이 다 삭제됨)
            binding.signUpCheckNumberEt.text.clear()

            return
        }

        // 인증번호 확인 API 연결
        val authService = UserRetrofitObj.getRetrofit().create(UserRetrofitItf::class.java)
        authService.mailNumber(CertificationNum(checkNum)).enqueue(object: Callback<UserRetrofitResponse> {
            override fun onResponse(call: Call<UserRetrofitResponse>, response: Response<UserRetrofitResponse>){
                Log.d("CHECK/SUCCESS", response.toString())
                val resp: UserRetrofitResponse = response.body()!!
                if (resp != null){
                    when(resp.isSuccess){
                        true -> signUpCheck(resp)
                        false -> Log.d("CHECK/SUCCESS", "인증 실패")
                    }
                } else {
                    Log.d("CHECK/SUCCESS", "Response body is null")
                }
            }

            override fun onFailure(call: Call<UserRetrofitResponse>, t: Throwable) {
                Log.d("CHECK/FAILURE", t.message.toString())
            }

        })

    }

    private fun signUpCheck(userResponse: UserRetrofitResponse){ // 인증 성공 확인하는 함수

        Log.d("message", userResponse.message)
        Log.d("result", userResponse.result)

        Toast.makeText(this, "인증을 성공했습니다!", Toast.LENGTH_SHORT).show()

        // 인증 성공 후 회원가입 진행
        signUp()
    }

    private fun signUp(){

        // 회원가입 진행 API 연결 // 회원가입에서 입력한 학번과 비밀번호를 전달
        val authService = UserRetrofitObj.getRetrofit().create(UserRetrofitItf::class.java)
        authService.join(studentId.toString(), password.toString()).enqueue(object: Callback<UserRetrofitResponse> {
            override fun onResponse(call: Call<UserRetrofitResponse>, response: Response<UserRetrofitResponse>){
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp: UserRetrofitResponse = response.body()!!
                if (resp != null){
                    when(resp.isSuccess){
                        true -> startActivity(resp)
                        false -> Log.d("SIGNUP/SUCCESS", "회원가입 진행 실패")
                    }
                } else {
                    Log.d("SIGNUP/SUCCESS", "Response body is null")
                }
            }

            override fun onFailure(call: Call<UserRetrofitResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }

        })
    }

    private fun startActivity(userResponse: UserRetrofitResponse){

        Log.d("message", userResponse.message)
        Log.d("result", userResponse.result)

        // 회원가입 진행 완료 후 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)

        // 슬라이드 효과 적용
        val options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_in_left)
        startActivity(intent, options.toBundle())

    }


}