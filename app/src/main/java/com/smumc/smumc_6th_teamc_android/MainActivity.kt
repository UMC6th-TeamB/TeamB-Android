package com.smumc.smumc_6th_teamc_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smumc.smumc_6th_teamc_android.chat.ChatActivity
import com.smumc.smumc_6th_teamc_android.chat.ChatMenuActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityMainBinding
import com.smumc.smumc_6th_teamc_android.login.LoginActivity
import com.smumc.smumc_6th_teamc_android.login.SignUpActivity
import com.smumc.smumc_6th_teamc_android.mypage.MypageActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent1 = Intent(this, ChatMenuActivity::class.java)
        intent1.putExtra("DISPLAY_MODE", 1)
        startActivity(intent1)
    }
}
