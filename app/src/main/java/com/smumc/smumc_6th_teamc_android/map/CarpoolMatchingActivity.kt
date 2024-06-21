package com.smumc.smumc_6th_teamc_android.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityCarpoolMatchingBinding

class CarpoolMatchingActivity : AppCompatActivity(){
    lateinit var binding: ActivityCarpoolMatchingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarpoolMatchingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}