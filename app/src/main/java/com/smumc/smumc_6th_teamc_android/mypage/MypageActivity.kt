package com.smumc.smumc_6th_teamc_android.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMypageBinding

class MypageActivity : AppCompatActivity() {

    lateinit var binding: ActivityMypageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}