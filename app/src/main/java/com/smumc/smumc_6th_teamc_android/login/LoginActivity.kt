package com.smumc.smumc_6th_teamc_android.login

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // "회원가입" 텍스트에 밑줄 추가
        binding.signUpBt.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        // 회원가입 버튼 클릭 시 SingUpActivity(회원가입 화면)으로 이동
        binding.signUpBt.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}