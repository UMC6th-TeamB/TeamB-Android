package com.smumc.smumc_6th_teamc_android.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivitySelectStationBinding

class SelectStationActivity : AppCompatActivity(){
    lateinit var binding: ActivitySelectStationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectStationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}