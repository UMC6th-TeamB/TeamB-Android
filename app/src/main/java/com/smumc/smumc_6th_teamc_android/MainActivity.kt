package com.smumc.smumc_6th_teamc_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smumc.smumc_6th_teamc_android.chat.ChatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ChatActivity를 DISPLAY_MODE 값 1을 전달하며 시작
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("DISPLAY_MODE", 1)
        }
        startActivity(intent)
    }
}
