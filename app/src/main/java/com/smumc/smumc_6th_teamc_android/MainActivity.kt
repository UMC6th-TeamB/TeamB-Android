package com.smumc.smumc_6th_teamc_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMainBinding
import com.smumc.smumc_6th_teamc_android.login.LoginActivity
import com.smumc.smumc_6th_teamc_android.login.SignUpActivity
import com.smumc.smumc_6th_teamc_android.mypage.MypageActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
}