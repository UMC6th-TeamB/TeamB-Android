package com.smumc.smumc_6th_teamc_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}