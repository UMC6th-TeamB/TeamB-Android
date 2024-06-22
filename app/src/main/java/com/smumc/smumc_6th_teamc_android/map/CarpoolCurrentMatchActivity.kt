package com.smumc.smumc_6th_teamc_android.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityCarpoolCurrentMatchBinding

class CarpoolCurrentMatchActivity : AppCompatActivity(){
    lateinit var binding: ActivityCarpoolCurrentMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarpoolCurrentMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}