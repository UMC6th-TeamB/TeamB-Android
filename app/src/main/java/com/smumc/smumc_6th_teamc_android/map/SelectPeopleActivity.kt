package com.smumc.smumc_6th_teamc_android.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivitySelectPeopleBinding

class SelectPeopleActivity : AppCompatActivity(){
    lateinit var binding: ActivitySelectPeopleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}