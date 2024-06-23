package com.smumc.smumc_6th_teamc_android.chat

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory

object RestClient {

    private const val BASE_URL = "http://43.201.182.155:8080/"

//    @Volatile
//    private var instance: RestClient? = null
//    private val lock = Any()

//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8080/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .build()
//    }

//    val exampleRepository: ChatRetrofitInterfaces by lazy {
//        retrofit.create(ChatRetrofitInterfaces::class.java)
//    }

    fun getRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }
}