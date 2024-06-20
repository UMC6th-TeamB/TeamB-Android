package com.smumc.smumc_6th_teamc_android.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ActivitySignUpCheckBinding

class SignUpCheckActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignUpCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SignUpActivity에서 학번을 전달 받음
        val studentId = intent.getStringExtra("studentId")

        // 전달 받은 학번으로 TextView 변경
        binding.userIdTv.text = studentId

        // 회원가입 버튼 클릭 시
        binding.signUpStartBt.setOnClickListener {

            // 인증번호를 확인하는 함수 호출 (singCheckUp)
            if(signCheckUp()){ // true: 올바르게 입력 시 회원가입 진행 함수 호출 (singUp)
                singUp()
            }
            else { // false: 올바르게 입력하지 않을 시 다시 입력하는 함수 호출 (resignUp)
                resignUp()
            }
        }

    }

    private fun signCheckUp(): Boolean { // 인증번호 입력 확인 함수

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

    private fun resignCheckUp(): Boolean { // 인증번호 입력 확인 함수 (1번 이상 비정상적으로 입력했을 경우)

        // 인증번호를 입력하지 않은 경우 (빈칸) (signUpStudentNumberEtEr 텍스트로 확인)
        if (binding.signUpCheckNumberEtEr.text.toString().isEmpty()){

            // 인증번호 (visible or gone)
            binding.signUpCheckNumberEt.visibility = View.GONE
            binding.signUpCheckNumberEtEr.visibility = View.VISIBLE

            // 입력 오류 시 멘트 (visible)
            binding.checkError.visibility = View.VISIBLE

            // 회원가입이 실패했으므로 EditText를 비워줌
            binding.signUpCheckNumberEtEr.text.clear()


            return false
        }

        return true
    }

    private fun singUp(){ // 회원가입 진행 함수 (처음부터 정상적으로 입력했을 경우)0
        // 회원가입 진행 완료 후 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)

        // 슬라이드 효과 적용
        val options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_in_left)
        startActivity(intent, options.toBundle())

        Toast.makeText(this, "인증을 성공했습니다!", Toast.LENGTH_SHORT).show()
        //finish()
    }

    private fun resignUp(){ // 회원가입 진행 함수 (1번 이상 비정상적으로 입력했을 경우)
        // xml에서 editText 입력이 signUpCheckNumberEt, signUpCheckNumberEtEr 두 가지로 나누어져 있기 때문에
        // 1번 이상 비정상적으로 입력했을 경우 signUpCheckNumberEtEr 입력
        if(resignCheckUp()){ //true: 회원가입 진행
            // 회원가입 진행 완료 후 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)

            // 슬라이드 효과 적용
            val options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_in_left)
            startActivity(intent, options.toBundle())

            Toast.makeText(this, "인증을 성공했습니다!", Toast.LENGTH_SHORT).show()
            //finish()
        }

    }


}