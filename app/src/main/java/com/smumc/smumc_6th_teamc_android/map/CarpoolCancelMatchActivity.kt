package com.smumc.smumc_6th_teamc_android.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityCarpoolCancelMatchBinding

class CarpoolCancelMatchActivity : AppCompatActivity(){
    lateinit var binding: ActivityCarpoolCancelMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarpoolCancelMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}