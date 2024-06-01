package com.smumc.smumc_6th_teamc_android.login

import androidx.room.Entity
import androidx.room.PrimaryKey

// 데이터 클래스: 사용자 정보
@Entity(tableName = "UserTable")
data class User(
    var studentId : String, //학번
    var password : String //비번
){
<<<<<<< HEAD
    // 사용자가 추가될 때마다 자동적으로 카운트
=======
>>>>>>> 99f318c (feat: 회원가입 및 로그인 인증 구현)
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
