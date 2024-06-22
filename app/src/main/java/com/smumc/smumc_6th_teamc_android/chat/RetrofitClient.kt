package com.smumc.smumc_6th_teamc_android.chat

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// RetrofitClient 객체 정의: Retrofit 인스턴스를 생성하고 관리
object RetrofitClient {

    private const val BASE_URL = "https://yourbackend.com/api/" // 기본 URL 설정

    // HttpLoggingInterceptor 생성 및 설정: 네트워크 요청 및 응답을 로그로 출력
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 로그 레벨을 BODY로 설정하여 전체 바디 출력
    }

    // OkHttpClient 생성 및 설정: HttpLoggingInterceptor를 추가
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // 로깅 인터셉터 추가
        .build()

    // ApiService 인스턴스를 지연 초기화(lazy) 방식으로 생성
    val instance: ApiService by lazy {
        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // 기본 URL 설정
            .client(httpClient) // OkHttpClient 설정
            .addConverterFactory(GsonConverterFactory.create()) // GsonConverterFactory 추가
            .build()
        retrofit.create(ApiService::class.java) // ApiService 인터페이스 구현체 생성
    }
}
