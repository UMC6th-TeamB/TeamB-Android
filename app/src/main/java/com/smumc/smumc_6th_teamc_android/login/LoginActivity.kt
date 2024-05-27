package com.smumc.smumc_6th_teamc_android.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}