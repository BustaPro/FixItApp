package com.example.fixitapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExternalRetrofitClient {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val apiService: ExternalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExternalApiService::class.java)
    }
}
