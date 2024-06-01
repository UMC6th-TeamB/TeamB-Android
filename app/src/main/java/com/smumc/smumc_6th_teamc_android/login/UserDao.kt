package com.smumc.smumc_6th_teamc_android.login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers(): List<User> // 사용자의 모든 정보를 리스트로 가져옴

<<<<<<< HEAD
    @Query("SELECT * FROM UserTable WHERE studentId = :studentId AND password = :password")
    fun getUser(studentId: String, password: String) : User? // 한명의 사용자 정보만 가져옴
=======

    @Query("SELECT * FROM UserTable WHERE studentId = :studentId AND password = :password")
    fun getUser(studentId: String, password: String) : User? // 한명의 사용자 정보만 가져옴

>>>>>>> 99f318c (feat: 회원가입 및 로그인 인증 구현)
}