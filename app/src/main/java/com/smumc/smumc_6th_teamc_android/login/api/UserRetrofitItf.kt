package com.smumc.smumc_6th_teamc_android.login.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserRetrofitItf {

    // 이메일 인증 url
    @POST("mail/check")
    fun email(@Body client: Client): Call<UserRetrofitResponse>

    // 인증번호 일치 확인 url
    @POST("mail/authentication")
    fun mailNumber(@Body certificationNum: CertificationNum): Call<UserRetrofitResponse>

    // 회원가입 진행 url
    @FormUrlEncoded
    @POST("join")
    fun join(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<UserRetrofitResponse>

    // 로그인 url
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<UserRetrofitResponse>


    // 마이페이지 url
    @GET("/user")
    fun myPage(@Header("Authorization") token: String): Call<UserRetrofitResponse>

    // 닉네임 수정 url
    @PATCH("/user")
    fun changeNickname(
        @Header("Authorization") token: String,
        @Body nickname: Nickname
    ): Call<UserRetrofitResponse>

}