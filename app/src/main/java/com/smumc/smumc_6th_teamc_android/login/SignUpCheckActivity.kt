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
import androidx.core.content.ContextCompat
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ActivitySignUpCheckBinding
import kotlin.concurrent.thread

class SignUpCheckActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignUpCheckBinding
    private var total = 180 // 3분 타이머
    private var started = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SignUpActivity에서 학번을 전달 받음
        val studentId = intent.getStringExtra("studentId")

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
            if(signCheckUp()){ // 인증번호 확인하는 함수 호출 (signCheckUp)
                singUp() // 올바르게 입력 시 회원가입 진행
            }
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

    private fun signCheckUp(): Boolean { // 인증번호 확인하는 함수

        // 인증번호를 입력하지 않은 경우 (빈칸)
        // 현재로선 빈칸 입력 시 오류 발생하는 것으로 구현했습니다.
        if (binding.signUpCheckNumberEt.text.toString().isEmpty()){

            // 인증번호 (visible or gone)
            binding.signUpCheckNumberEt.visibility = View.GONE
            binding.signUpCheckNumberEtEr.visibility = View.VISIBLE

            // 입력 오류 시 멘트 (visible)
            binding.checkError.visibility = View.VISIBLE

            // 회원가입이 실패했으므로 EditText를 비워줌 (사용자가 입력한 값이 다 삭제됨)
            binding.signUpCheckNumberEt.text.clear()

            return false
        }

        return true
    }

    private fun singUp(){ // 회원가입 진행 함수
        // 회원가입 진행 완료 후 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "인증을 성공했습니다!", Toast.LENGTH_SHORT).show()
        //finish()
    }
}