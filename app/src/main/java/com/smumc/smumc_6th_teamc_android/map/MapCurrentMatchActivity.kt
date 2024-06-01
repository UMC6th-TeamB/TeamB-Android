package com.smumc.smumc_6th_teamc_android.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMapCurrentMatchBinding

class MapCurrentMatchActivity : AppCompatActivity(){
    lateinit var binding: ActivityMapCurrentMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapCurrentMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}